package infrastructure;

import infrastructure.anotation.DemonThread;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.NetworkDto;
import infrastructure.anotation.Singleton;
import infrastructure.anotation.TcpEndpoint;
import infrastructure.anotation.rest.RestDelete;
import infrastructure.anotation.rest.RestEndpoint;
import infrastructure.anotation.rest.RestGetAll;
import infrastructure.anotation.rest.RestGetById;
import infrastructure.anotation.rest.RestPut;
import infrastructure.anotation.rest.RestUpdate;
import infrastructure.currency.CurrencyInfo;
import infrastructure.currency.CurrencyInfoFromFileLoader;
import infrastructure.currency.CurrencyInfoLoader;
import infrastructure.exception.ReflectionException;
import infrastructure.factory.ObjectFactory;
import infrastructure.factory.ObjectFactoryImpl;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.http.controller.PhantomController;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.util.RestUrlUtilService;
import infrastructure.config.Config;
import infrastructure.config.JavaConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Represents info about context in which application run.
 * Contains info about currency rates.
 * Controls beans and commands.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ApplicationContextImpl implements ApplicationContext {
    private static final Logger log = LogManager.getLogger(ApplicationContextImpl.class);
    private final Map<Class, Object> objectsCash;
    private final Map<String, MultipleMethodController> controllerMap;
    private final Map<String, RestProcessorMethodsInfo> urlMatchPatternToCommandProcessorInfo;
    private final Map<String, CurrencyInfo> currencies;
    private final Map<String, TcpController> messageTypeTcpCommandControllerMap;
    private final Map<String, Class> messageTypeToMessageClass;
    private final Map<Class, String> massageClassToCode;
    private final Class defaultEndpoint = PhantomController.class;
    private final Config config;
    private ObjectFactory factory;
    private final ResourceBundle applicationConfigurationBundle = ResourceBundle.getBundle("application");

    private static ApplicationContext singleToneApplicationContext;

    public static ApplicationContext getContext() {
        if (singleToneApplicationContext == null) {
            synchronized (ApplicationContext.class) {
                if (singleToneApplicationContext == null) {
                    Map<Class, Object> paramMap = new ConcurrentHashMap<>();
                    ApplicationContext context = new ApplicationContextImpl(new JavaConfig(""), paramMap,
                            new ConcurrentHashMap<>(), new CurrencyInfoFromFileLoader(), new ConcurrentHashMap<>(), new HashMap<>(), new HashMap<>());//todo ivan perhaps we do not need here concurrent hash maps
                    ObjectFactory objectFactory = new ObjectFactoryImpl(context);
                    context.setFactory(objectFactory);
                    context.init();
                    singleToneApplicationContext = context;
                }
            }
        }
        return singleToneApplicationContext;
    }

    /**
     * @param config                             config instance
     * @param preparedCash                       preload objects map. May be used for load classes which have more then one implementation
     * @param controllersPrepared                preload controllers map.
     * @param currencyInfoLoader                 used in constructor to load into {@link ApplicationContextImpl#currencies} exchange rates
     * @param messageTypeTcpCommandControllerMap
     * @param messageTypeToMessageClass
     * @param massageClassToCode
     */
    private ApplicationContextImpl(Config config, Map<Class, Object> preparedCash,
                                   Map<String, MultipleMethodController> controllersPrepared,
                                   CurrencyInfoLoader currencyInfoLoader, Map<String, TcpController> messageTypeTcpCommandControllerMap, Map<String, Class> messageTypeToMessageClass, Map<Class, String> massageClassToCode) {
        this.messageTypeTcpCommandControllerMap = messageTypeTcpCommandControllerMap;
        this.messageTypeToMessageClass = messageTypeToMessageClass;
        this.massageClassToCode = massageClassToCode;
        log.debug("");

        this.controllerMap = controllersPrepared;
        this.config = config;
        this.objectsCash = preparedCash;
        currencies = currencyInfoLoader.getCurrencyInfo();
        urlMatchPatternToCommandProcessorInfo = new HashMap<>();
    }

    /**
     * used to preload not lazy singletons
     */
    public void init() {
        objectsCash.put(ApplicationContext.class, this);
        log.debug("");

        for (Class<?> clazz : config.getTypesAnnotatedWith(Singleton.class)) {
            Singleton annotation = clazz.getAnnotation(Singleton.class);
            if (!annotation.isLazy()) {
                log.debug("created" + clazz.getName());
                getObject(clazz);
            }
        }

        for (Class<?> clazz : config.getTypesAnnotatedWith(NetworkDto.class)) {
            NetworkDto annotation = clazz.getAnnotation(NetworkDto.class);
            messageTypeToMessageClass.put(annotation.massageCode(), clazz);
            massageClassToCode.put(clazz, annotation.massageCode());
        }

        for (Class<?> clazz : config.getImplClasses(Runnable.class)) {
            if (Arrays.stream(clazz.getAnnotations()).anyMatch(annotation -> annotation instanceof DemonThread)) {
                final Runnable runnable = (Runnable) getObject(clazz);
                new Thread(runnable).start();
            }
        }

    }

//    @Override
//    @SneakyThrows
//    public WebSocketMessageSender createClientWebSocketConnection(String serverPath){
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//        final Session session = container.connectToServer(ClientWebSocketHandler.class,
//                new URI("serverPath"));//ws://localhost:8080/balanser
//        return new WebSocketMessageSender(this, session, new MassageEncoder());
//    }

    public CurrencyInfo getCurrencyInfo(String langKey) {
        return currencies.get(langKey);
    }

    @Override
    public <T> void addObject(Class<T> typeKey, Object object) {
        if (!objectsCash.containsKey(typeKey)) {
            synchronized (objectsCash) {
                if (!objectsCash.containsKey(typeKey)) {
                    objectsCash.put(typeKey, object);
                }
            }
        }

    }

    public <T> T getObject(Class<T> typeKey) {
        log.debug("");

        if (objectsCash.containsKey(typeKey)) {
            return (T) objectsCash.get(typeKey);
        }
        synchronized (objectsCash) {
            if (objectsCash.containsKey(typeKey)) {
                return (T) objectsCash.get(typeKey);
            }
            Class<? extends T> implClass = typeKey;

            if (typeKey.isInterface()) {
                implClass = config.getImplClass(typeKey);
            }
            T toReturn;
            try {
                toReturn = factory.createObject(implClass);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new ReflectionException(e);
            }
            putToObjectsCashIfSingleton(typeKey, implClass, toReturn);
            return toReturn;
        }
    }

    public MultipleMethodController getHttpCommand(String linkKey) {
        log.debug("");

        if (controllerMap.containsKey(linkKey)) {
            return controllerMap.get(linkKey);
        }
        synchronized (controllerMap) {
            if (controllerMap.containsKey(linkKey)) {
                return controllerMap.get(linkKey);
            }
            for (Class<?> clazz : config.getTypesAnnotatedWith(
                    HttpEndpoint.class)) {//todo make consider setting this mup on startup it may slow down upping system but removes necessary to run this code for each Tcp controller
                HttpEndpoint annotation = clazz.getAnnotation(HttpEndpoint.class);
                for (String i : annotation.value()) {
                    if (i.equals(linkKey)) {
                        MultipleMethodController toReturn = (MultipleMethodController) getObject(clazz);
                        putToHttpCommandMapIfSingleton(linkKey, clazz, toReturn);
                        return toReturn;
                    }
                }
            }
        }
        return (MultipleMethodController) getObject(defaultEndpoint);
    }


    @Override
    public RestUrlCommandProcessorInfo getRestCommand(String requestUrl, String requestMethod) {
        log.debug("");

        Optional<String> restKey = getRestUrlMatcherKeyToComandProcessor(requestUrl);
        if (restKey.isPresent()) {
            RestProcessorMethodsInfo restProcessorMethodsInfo = urlMatchPatternToCommandProcessorInfo.get(restKey.get());
            return RestUrlCommandProcessorInfo.builder()
                    .commandProcessor(restProcessorMethodsInfo.getCommandProcessor())
                    .processorsMethod(RestUrlUtilService.retrieveMethodForProcessRequest(requestUrl, requestMethod, restProcessorMethodsInfo))
                    .restUrlVariableInfos(restProcessorMethodsInfo.getRestUrlVariableInfos())
                    .build();
        }
        synchronized (urlMatchPatternToCommandProcessorInfo) {
            restKey = getRestUrlMatcherKeyToComandProcessor(requestUrl);
            if (restKey.isPresent()) {
                RestProcessorMethodsInfo restProcessorMethodsInfo = urlMatchPatternToCommandProcessorInfo.get(restKey.get());
                return RestUrlCommandProcessorInfo.builder()
                        .commandProcessor(restProcessorMethodsInfo.getCommandProcessor())
                        .processorsMethod(RestUrlUtilService.retrieveMethodForProcessRequest(requestUrl, requestMethod, restProcessorMethodsInfo))
                        .restUrlVariableInfos(restProcessorMethodsInfo.getRestUrlVariableInfos())
                        .build();
            }
            return getNotCashedYetRestUrlCommandProcessorInfo(requestUrl, requestMethod);
        }
    }

    private RestUrlCommandProcessorInfo getNotCashedYetRestUrlCommandProcessorInfo(String requestUrl, String requestMethod) {
        for (Class<?> clazz : config.getTypesAnnotatedWith(RestEndpoint.class)) {
            RestEndpoint annotation = clazz.getAnnotation(RestEndpoint.class);
            for (String resourceFromAnnotation : annotation.resource()) {
                String[] urlSteps = resourceFromAnnotation.split("\\/");
                urlSteps = Arrays.copyOfRange(urlSteps, 1, urlSteps.length);
                String resourceWithoutLustIdParameterRegex = removeLustIdParameter(resourceFromAnnotation).replaceAll("\\/\\{[^/]*\\}\\/", "/[^/]*/");
                String restCommandPattern = resourceFromAnnotation.replaceAll("\\/\\{[^/]*\\}\\/", "/[^/]*/")
                        .replaceAll("\\/\\{[^/]*\\}", "/?([^/])*");//change to make match any.
                //+ "\\/\\w*";//add end matcher //no need to add and matcher the id should be in url
                if (requestUrl.matches(restCommandPattern)) {
                    Object commandProcessor = getObject(clazz);
                    if (clazz.isAnnotationPresent(Singleton.class)) {//по хорошему нужно создать кеш в не зависимости от синглтонства. Потому что каждый раз для
                        cashRestSingeltonComandProcessor(clazz, restCommandPattern, resourceWithoutLustIdParameterRegex, commandProcessor, urlSteps);
                    }
                    return RestUrlCommandProcessorInfo.builder()
                            .commandProcessor(commandProcessor)
                            .restUrlVariableInfos(getRestUrlVariableInfos(urlSteps))
                            .processorsMethod(
                                    config.getMethodAnnotatedWith(
                                            clazz, RestUrlUtilService.getRestMethodAnnotation(requestUrl, requestMethod, resourceWithoutLustIdParameterRegex))
                            ).build();
                }
            }
        }
        return null;//(MultipleMethodController) getObject(defaultEndpoint); todo сделать дефолтный обработчки 404 на случай если не нашелся подходящий мапинг
    }

    private String removeLustIdParameter(String resourceFromAnnotation) {
        String resourceWithoutLustIdParameter = resourceFromAnnotation;//отбрасываем последний парметр в урле
        int lastIndex = resourceWithoutLustIdParameter.lastIndexOf("/");
        if (lastIndex != -1) {
            resourceWithoutLustIdParameter = resourceFromAnnotation.substring(0, lastIndex);
        }
        return resourceWithoutLustIdParameter;
    }

    private List<RestUrlVariableInfo> getRestUrlVariableInfos(String[] urlSteps) {
        List<RestUrlVariableInfo> restUrlVariableInfos = new ArrayList<>();
        for (int j = 0; j < urlSteps.length; j++) {
            if (urlSteps[j].matches("\\{[^/]*\\}")) {
                restUrlVariableInfos.add(new RestUrlVariableInfo(j, urlSteps[j].substring(1, urlSteps[j].length() - 1)));
            }
        }
        return restUrlVariableInfos;
    }

    private void cashRestSingeltonComandProcessor(Class<?> clazz, String resourceUrlPattern, String pureEndingOfResource, Object commandProcessor, String[] urlSteps) {
        RestProcessorMethodsInfo restUrlCommandProcessorInfo = new RestProcessorMethodsInfo();
        restUrlCommandProcessorInfo.setCommandProcessor(commandProcessor);
        restUrlCommandProcessorInfo.setPureEndingOfResource(pureEndingOfResource);
        restUrlCommandProcessorInfo.setGetByIdMethod(config.getMethodAnnotatedWith(clazz, RestGetById.class));
        restUrlCommandProcessorInfo.setGetAllMethod(config.getMethodAnnotatedWith(clazz, RestGetAll.class));
        restUrlCommandProcessorInfo.setUpdateMethod(config.getMethodAnnotatedWith(clazz, RestUpdate.class));
        restUrlCommandProcessorInfo.setPutMethod(config.getMethodAnnotatedWith(clazz, RestPut.class));
        restUrlCommandProcessorInfo.setDeleteMethod(config.getMethodAnnotatedWith(clazz, RestDelete.class));
        restUrlCommandProcessorInfo.setRestUrlVariableInfos(getRestUrlVariableInfos(urlSteps));
        urlMatchPatternToCommandProcessorInfo.put(resourceUrlPattern, restUrlCommandProcessorInfo);
    }

    private Optional<String> getRestUrlMatcherKeyToComandProcessor(String linkKey) {
        return urlMatchPatternToCommandProcessorInfo.keySet().stream().filter(linkKey::matches).findFirst();
    }

    @Override
    public TcpController getTcpCommandController(String requestMessageType) {
        if (messageTypeTcpCommandControllerMap.containsKey(requestMessageType)) {
            return messageTypeTcpCommandControllerMap.get(requestMessageType);
        }
        synchronized (messageTypeTcpCommandControllerMap) {
            if (messageTypeTcpCommandControllerMap.containsKey(requestMessageType)) {
                return messageTypeTcpCommandControllerMap.get(requestMessageType);
            }
            for (Class<?> clazz : config.getTypesAnnotatedWith(TcpEndpoint.class)) {//todo make consider setting this mup on startup it may slow down upping system but removes necessary to run this code for each Tcp controller
                TcpEndpoint annotation = clazz.getAnnotation(TcpEndpoint.class);
                if (requestMessageType.equals(annotation.requestMessageCode())) {
                    TcpController toReturn = (TcpController) getObject(clazz);
                    putToTcpCommandMapIfSingleton(requestMessageType, clazz, toReturn);
                    return toReturn;
                }
            }
        }
        return (TcpController) getObject(defaultEndpoint);
    }

    @Override
    public Class getMessageTypeByCode(String messageCode) {
        return messageTypeToMessageClass.get(messageCode);
    }

    @Override
    public String getMessageCodeByType(Object messageType) {
        return massageClassToCode.get(messageType);
    }

    @Override
    public ResourceBundle getApplicationConfigurationBundle() {
        return applicationConfigurationBundle;
    }

    public void setFactory(ObjectFactory factory) {
        this.factory = factory;
    }

    public Config getConfig() {
        return this.config;
    }

    private <T> void putToObjectsCashIfSingleton(Class<T> type, Class<? extends T> implClass, T instance) {
        if (implClass.isAnnotationPresent(Singleton.class)) {
            objectsCash.put(type, instance);
        }
    }

    private void putToHttpCommandMapIfSingleton(String link, Class<?> clazz, MultipleMethodController toReturn) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            controllerMap.put(link, toReturn);
        }
    }

    private void putToTcpCommandMapIfSingleton(String messageType, Class<?> clazz, TcpController toReturn) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            messageTypeTcpCommandControllerMap.put(messageType, toReturn);
        }
    }
}
