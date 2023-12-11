package infrastructure;

import infrastructure.anotation.DemonThread;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.NetworkDto;
import infrastructure.anotation.Singleton;
import infrastructure.anotation.rest.RestDelete;
import infrastructure.anotation.rest.RestEndpoint;
import infrastructure.anotation.rest.RestGetAll;
import infrastructure.anotation.rest.RestGetById;
import infrastructure.anotation.rest.RestPut;
import infrastructure.anotation.rest.RestUpdate;
import infrastructure.config.Config;
import infrastructure.config.JavaConfig;
import infrastructure.currency.CurrencyInfo;
import infrastructure.currency.CurrencyInfoFromFileLoader;
import infrastructure.currency.CurrencyInfoLoader;
import infrastructure.exception.ReflectionException;
import infrastructure.factory.ObjectFactory;
import infrastructure.factory.ObjectFactoryImpl;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.http.controller.PhantomController;
import infrastructure.rest.Rest404Controller;
import infrastructure.soket.web_socket.ClientWebSocketHandler;
import infrastructure.soket.web_socket.controller.AbstractTcpController;
import infrastructure.soket.web_socket.controller.Error404DefaultController;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.soket.web_socket.dto.Error404Dto;
import infrastructure.util.RequestUtilService;
import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public static final String URL_STEP_DELIMITER = "/";
    public static final String REPLACEMENT_REGEX_FOR_REST_ENDPOINT_VARIABLE_PLACEHOLDER = "/";
    public static final String URL_STEP_SPLIT_REGEX = "\\" + URL_STEP_DELIMITER;
    public static final String REST_URL_VARIABLE_REGEX = "\\{[^/]*\\}";
    public static final String REST_ENDPOINT_VARIABLE_PLACEHOLDER_REGEX = "\\" + URL_STEP_DELIMITER + REST_URL_VARIABLE_REGEX + "\\" + URL_STEP_DELIMITER;
    public static final String REST_ENDPOINT_LUST_VARIABLE_PLACEHOLDER_REGEX = "\\" + URL_STEP_DELIMITER + REST_URL_VARIABLE_REGEX;
    public static final String REPLACEMENT_REGEX_FOR_REST_ENDPOINT_LUST_VARIABLE_PLACEHOLDER = "/";
    private final Map<Class<?>, Object> objectsCash;
    private final Map<String, Class<?>> controllerMap;//todo consider refactor this to hold type instead of link to object //todo consider rewrite it to annotation based approach
    private final Map<String, RestProcessorCashInfo> urlMatchPatternToCommandProcessorInfo;
    private final Map<String, CurrencyInfo> currencies;
    private final Map<String, Class<?>> messageTypeTcpCommandControllerMap;
    private final Map<String, Class<?>> messageTypeToMessageClass;
    private final Map<Class<?>, String> massageClassToCode;
    private final Set<Class<?>> processedCalsesSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Class defaultEndpoint = PhantomController.class;
    private Config config;
    private ObjectFactory factory;
    private final ResourceBundle applicationConfigurationBundle = ResourceBundle.getBundle("application");

    private static final ApplicationContext singleToneApplicationContext;

    static {
        Map<Class<?>, Object> paramMap = new ConcurrentHashMap<>();
        ApplicationContext context = new ApplicationContextImpl(paramMap,
                new ConcurrentHashMap<>(), new CurrencyInfoFromFileLoader(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());//todo ivan perhaps we do not need here concurrent hash maps
        ObjectFactory objectFactory = new ObjectFactoryImpl(context);
        context.setFactory(objectFactory);
        context.init();
        singleToneApplicationContext = context;
    }

    public static ApplicationContext getContext() {
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
    private ApplicationContextImpl(Map<Class<?>, Object> preparedCash,
                                   Map<String, Class<?>> controllersPrepared,
                                   CurrencyInfoLoader currencyInfoLoader, Map<String, Class<?>> messageTypeTcpCommandControllerMap, Map<String, Class<?>> messageTypeToMessageClass, Map<Class<?>, String> massageClassToCode) {
        this.messageTypeTcpCommandControllerMap = messageTypeTcpCommandControllerMap;
        this.messageTypeToMessageClass = messageTypeToMessageClass;
        this.massageClassToCode = massageClassToCode;
        log.debug("");

        this.controllerMap = controllersPrepared;
        this.objectsCash = preparedCash;
        currencies = currencyInfoLoader.getCurrencyInfo();
        urlMatchPatternToCommandProcessorInfo = new HashMap<>();
    }

    @Override
    public String getPropertyValue(String key) {
        return applicationConfigurationBundle.getString(key);
    }

    /**
     * used to preload not lazy singletons
     */
    public void init() {
        objectsCash.put(ApplicationContext.class, this);
        log.debug("");
        initSingletonAnnotatedObjects(true);
        initNetworkDto(true);
        initHttpControllers(true);
        initRestControllers(true);
        initTcpEndpoints(true);
        if (Boolean.parseBoolean(getPropertyValue("infrastructure.initialize.components.in.parallel"))){
            Thread thread = new Thread(()->initSingletonAnnotatedObjects(false));
            thread.setPriority(Integer.parseInt(getPropertyValue("infrastructure.initialize.components.in.parallel.priority")));
            thread.start();
            thread = new Thread(()->initNetworkDto(false));
            thread.setPriority(Integer.parseInt(getPropertyValue("infrastructure.initialize.components.in.parallel.priority")));
            thread.start();
            thread = new Thread(()->initHttpControllers(false));
            thread.setPriority(Integer.parseInt(getPropertyValue("infrastructure.initialize.components.in.parallel.priority")));
            thread.start();
            thread = new Thread(()->initRestControllers(false));
            thread.setPriority(Integer.parseInt(getPropertyValue("infrastructure.initialize.components.in.parallel.priority")));
            thread.start();
            thread = new Thread(()->initTcpEndpoints(false));
            thread.setPriority(Integer.parseInt(getPropertyValue("infrastructure.initialize.components.in.parallel.priority")));
            thread.start();
        }
        startDemonThreads();
    }

    private void startDemonThreads() {
        if (!Boolean.parseBoolean(getPropertyValue("infrastructure.include.in.start.demon.threads"))){
            return;
        }
        for (Class<?> clazz : getConfig().getTypesAnnotatedWith(DemonThread.class)) {
            final Runnable runnable = (Runnable) getObject(clazz);
            new Thread(runnable).start();
        }
    }

    private void initTcpEndpoints(boolean shouldCheckLazyLoad) {
        if (shouldCheckLazyLoad && !Boolean.parseBoolean(getPropertyValue("infrastructure.include.in.start.tcp.controllers"))){
            return;
        }
        for (Class<?> clazz : getConfig().getSubTypesOf(AbstractTcpController.class)) {
            cashTcpControllerData(clazz);
        }
    }

    private void initRestControllers(boolean shouldCheckLazyLoad) {
        if (shouldCheckLazyLoad && !Boolean.parseBoolean(getPropertyValue("infrastructure.include.in.start.rest.controllers"))){
            return;
        }
        for (Class<?> clazz : getConfig().getTypesAnnotatedWith(RestEndpoint.class)) {
            RestEndpoint annotation = clazz.getAnnotation(RestEndpoint.class);
            for (String resourceFromAnnotation : annotation.resource()) {
                String[] urlSteps = resourceFromAnnotation.split(URL_STEP_SPLIT_REGEX);
                urlSteps = Arrays.copyOfRange(urlSteps, 1, urlSteps.length);
                String restCommandPattern = resourceFromAnnotation.replaceAll(REST_ENDPOINT_VARIABLE_PLACEHOLDER_REGEX, REPLACEMENT_REGEX_FOR_REST_ENDPOINT_VARIABLE_PLACEHOLDER)
                        .replaceAll(REST_ENDPOINT_LUST_VARIABLE_PLACEHOLDER_REGEX, REPLACEMENT_REGEX_FOR_REST_ENDPOINT_LUST_VARIABLE_PLACEHOLDER);
                cashRestCommandProcessor(clazz, restCommandPattern, urlSteps);
                processedCalsesSet.add(clazz);
            }
        }
    }

    private void initHttpControllers(boolean shouldCheckLazyLoad) {
        if (shouldCheckLazyLoad && Boolean.parseBoolean(getPropertyValue("infrastructure.include.in.start.http.controllers"))) {
            for (Class<?> clazz : getConfig().getTypesAnnotatedWith(HttpEndpoint.class)) {
                HttpEndpoint annotation = clazz.getAnnotation(HttpEndpoint.class);
                Arrays.stream(annotation.value()).forEach(endpointUrl -> controllerMap.put(endpointUrl, clazz));
                processedCalsesSet.add(clazz);
            }
        }
    }
    private void initNetworkDto(boolean shouldCheckLazyLoad) {
        if (shouldCheckLazyLoad && Boolean.parseBoolean(getPropertyValue("infrastructure.include.in.start.network.dto"))) {
            for (Class<?> clazz : getConfig().getTypesAnnotatedWith(NetworkDto.class)) {
                NetworkDto annotation = clazz.getAnnotation(NetworkDto.class);
                messageTypeToMessageClass.put(annotation.massageCode(), clazz);
                massageClassToCode.put(clazz, annotation.massageCode());
                processedCalsesSet.add(clazz);
            }
        }
    }
    private void initSingletonAnnotatedObjects(boolean shouldCheckLazyLoad) {
        if (shouldCheckLazyLoad && Boolean.parseBoolean(getPropertyValue("infrastructure.include.in.start.singleton"))) {
            for (Class<?> clazz : getConfig().getTypesAnnotatedWith(Singleton.class)) {
                Singleton annotation = clazz.getAnnotation(Singleton.class);
                if (!annotation.isLazy()) {
                    log.debug("created" + clazz.getName());
                    getObject(clazz);
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public ClientWebSocketHandler createClientWebSocketConnection(String serverPath) {
        return new ClientWebSocketHandler(new URI("serverPath"));
    }

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
        if (objectsCash.containsKey(typeKey)) {
            return (T) objectsCash.get(typeKey);
        }
        synchronized (objectsCash) {
            if (objectsCash.containsKey(typeKey)) {
                return (T) objectsCash.get(typeKey);
            }
            Class<? extends T> implClass = typeKey;

            if (typeKey.isInterface()) {
                implClass = getConfig().getImplClass(typeKey);
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
            return (MultipleMethodController) getObject(controllerMap.get(linkKey));
        }
        synchronized (controllerMap) {
            if (controllerMap.containsKey(linkKey)) {
                return (MultipleMethodController) getObject(controllerMap.get(linkKey));
            }
            Set<Class<?>> typesToLookFor = getConfig().getTypesAnnotatedWith(HttpEndpoint.class).stream()
                    .filter(type -> !processedCalsesSet.contains(type))
                    .collect(Collectors.toSet());
            for (Class<?> clazz : typesToLookFor) {
                HttpEndpoint annotation = clazz.getAnnotation(HttpEndpoint.class);
                Arrays.stream(annotation.value()).forEach(endpointUrl -> controllerMap.put(endpointUrl, clazz));
                processedCalsesSet.add(clazz);
                for (String i : annotation.value()) {
                    if (i.equals(linkKey)) {
                        MultipleMethodController toReturn = (MultipleMethodController) getObject(clazz);
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

        KeyInfoRestRequest keyInfoRestRequest = getKeyInfoRestRequest(requestUrl);
        RestProcessorCashInfo restProcessorCashInfo = urlMatchPatternToCommandProcessorInfo.get(keyInfoRestRequest.resourceKey);
        if (restProcessorCashInfo != null) {
            return RestUrlCommandProcessorInfo.builder()
                    .commandProcessor(getObject(restProcessorCashInfo.getCommandProcessor()))
                    .processorsMethod(RequestUtilService.retrieveMethodForProcessRequest(keyInfoRestRequest.lustUrlStep, requestMethod, restProcessorCashInfo,
                                                                                         keyInfoRestRequest.resourceKey))
                    .restUrlVariableInfos(restProcessorCashInfo.getRestUrlVariableInfos())
                    .build();
        }

        synchronized (urlMatchPatternToCommandProcessorInfo) {
            restProcessorCashInfo = urlMatchPatternToCommandProcessorInfo.get(keyInfoRestRequest.resourceKey);
            if (restProcessorCashInfo != null) {
                return RestUrlCommandProcessorInfo.builder()
                        .commandProcessor(getObject(restProcessorCashInfo.getCommandProcessor()))
                        .processorsMethod(
                                RequestUtilService.retrieveMethodForProcessRequest(keyInfoRestRequest.lustUrlStep, requestMethod, restProcessorCashInfo,
                                                                                   keyInfoRestRequest.resourceKey))
                        .restUrlVariableInfos(restProcessorCashInfo.getRestUrlVariableInfos())
                        .build();
            }
        }
        return getNotCashedYetRestUrlCommandProcessorInfo(requestUrl, requestMethod);
    }

    private RestUrlCommandProcessorInfo getNotCashedYetRestUrlCommandProcessorInfo(String requestUrl, String requestMethod) {
        Set<Class<?>> typesToLookFor = getConfig().getTypesAnnotatedWith(RestEndpoint.class).stream()
                .filter(type -> !processedCalsesSet.contains(type))
                .collect(Collectors.toSet());
        for (Class<?> clazz : typesToLookFor) {
            RestEndpoint annotation = clazz.getAnnotation(RestEndpoint.class);
            for (String resourceFromAnnotation : annotation.resource()) {
                String[] urlSteps = resourceFromAnnotation.split(URL_STEP_SPLIT_REGEX);
                urlSteps = Arrays.copyOfRange(urlSteps, 1, urlSteps.length);
                String restCommandKey = resourceFromAnnotation.replaceAll(REST_ENDPOINT_VARIABLE_PLACEHOLDER_REGEX,
                                                                          REPLACEMENT_REGEX_FOR_REST_ENDPOINT_VARIABLE_PLACEHOLDER)
                        .replaceAll(REST_ENDPOINT_LUST_VARIABLE_PLACEHOLDER_REGEX, REPLACEMENT_REGEX_FOR_REST_ENDPOINT_LUST_VARIABLE_PLACEHOLDER);
                cashRestCommandProcessor(clazz, restCommandKey, urlSteps);
                typesToLookFor.add(clazz);
                KeyInfoRestRequest keyInfoRestRequest = getKeyInfoRestRequest(requestUrl);
                if (keyInfoRestRequest.resourceKey.equals(restCommandKey)) {

                    return RestUrlCommandProcessorInfo.builder()
                            .commandProcessor(getObject(clazz))
                            .restUrlVariableInfos(getRestUrlVariableInfos(urlSteps))
                            .processorsMethod(
                                    getConfig().getMethodAnnotatedWith(
                                            clazz,
                                            RequestUtilService.getRestMethodAnnotation(keyInfoRestRequest.lustUrlStep, requestMethod, restCommandKey))
                            ).build();
                }
            }
        }
        return RestUrlCommandProcessorInfo.builder().commandProcessor(new Rest404Controller())
                .processorsMethod(getConfig().getMethodAnnotatedWith(
                        Rest404Controller.class, RestGetAll.class))
                .build();
    }

    private KeyInfoRestRequest getKeyInfoRestRequest(String requestUrl) {
        String[] split = requestUrl.split(URL_STEP_SPLIT_REGEX);
        String restUrlWithoutVariables = "";
        for (int i = 1; i < split.length; i += 2) {
            restUrlWithoutVariables += URL_STEP_DELIMITER + split[i];
        }
        restUrlWithoutVariables += URL_STEP_DELIMITER;
        return new KeyInfoRestRequest(split[split.length - 1] + URL_STEP_DELIMITER, restUrlWithoutVariables);
    }

    private static class KeyInfoRestRequest {
        public final String lustUrlStep;
        public final String resourceKey;

        public KeyInfoRestRequest(String lustUrlStep, String resourceKey) {
            this.lustUrlStep = lustUrlStep;
            this.resourceKey = resourceKey;
        }
    }

    private String removeLustIdParameter(String resourceFromAnnotation) {
        String resourceWithoutLustIdParameter = resourceFromAnnotation;//отбрасываем последний парметр в урле
        int lastIndex = resourceWithoutLustIdParameter.lastIndexOf(URL_STEP_DELIMITER);
        if (lastIndex != -1) {
            resourceWithoutLustIdParameter = resourceFromAnnotation.substring(0, lastIndex);
        }
        return resourceWithoutLustIdParameter;
    }

    private List<RestUrlVariableInfo> getRestUrlVariableInfos(String[] urlSteps) {
        List<RestUrlVariableInfo> restUrlVariableInfos = new ArrayList<>();
        for (int j = 0; j < urlSteps.length; j++) {
            if (urlSteps[j].matches(REST_URL_VARIABLE_REGEX)) {
                restUrlVariableInfos.add(new RestUrlVariableInfo(j, urlSteps[j].substring(1, urlSteps[j].length() - 1)));
            }
        }
        return restUrlVariableInfos;
    }

    private void cashRestCommandProcessor(Class<?> clazz, String resourceUrlKey, String[] urlSteps) {
        RestProcessorCashInfo restUrlCommandProcessorInfo = new RestProcessorCashInfo();
        restUrlCommandProcessorInfo.setCommandProcessor(clazz);
        restUrlCommandProcessorInfo.setGetByIdMethod(getConfig().getMethodAnnotatedWith(clazz, RestGetById.class));
        restUrlCommandProcessorInfo.setGetAllMethod(getConfig().getMethodAnnotatedWith(clazz, RestGetAll.class));
        restUrlCommandProcessorInfo.setUpdateMethod(getConfig().getMethodAnnotatedWith(clazz, RestUpdate.class));
        restUrlCommandProcessorInfo.setPutMethod(getConfig().getMethodAnnotatedWith(clazz, RestPut.class));
        restUrlCommandProcessorInfo.setDeleteMethod(getConfig().getMethodAnnotatedWith(clazz, RestDelete.class));
        restUrlCommandProcessorInfo.setRestUrlVariableInfos(getRestUrlVariableInfos(urlSteps));
        urlMatchPatternToCommandProcessorInfo.put(resourceUrlKey, restUrlCommandProcessorInfo);
    }

    @Override
    public TcpController getTcpCommandController(String requestMessageType) {
        if (messageTypeTcpCommandControllerMap.containsKey(requestMessageType)) {
            return (TcpController) getObject(messageTypeTcpCommandControllerMap.get(requestMessageType));
        }
        synchronized (messageTypeTcpCommandControllerMap) {
            if (messageTypeTcpCommandControllerMap.containsKey(requestMessageType)) {
                return (TcpController) getObject(messageTypeTcpCommandControllerMap.get(requestMessageType));
            }
            Set<Class<?>> typesToLookFor = getConfig().getSubTypesOf(AbstractTcpController.class).stream()
                    .filter(type -> !processedCalsesSet.contains(type))
                    .collect(Collectors.toSet());
            for (Class<?> clazz : typesToLookFor) {
                String massageCode = cashTcpControllerData(clazz);
                if (requestMessageType.equals(massageCode)) {
                    TcpController toReturn = (TcpController) getObject(clazz);
                    return toReturn;
                }
            }
        }
        return getObject(Error404DefaultController.class);
    }

    private String cashTcpControllerData(Class<?> clazz) {
        ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();
        Class<?> actualTypeArgument = (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        String massageCode = actualTypeArgument.getAnnotation(NetworkDto.class).massageCode();
        messageTypeTcpCommandControllerMap.put(massageCode, clazz);
        putTcpMessageToCash(actualTypeArgument, massageCode);
        processedCalsesSet.add(clazz);
        return massageCode;
    }

    private void putTcpMessageToCash(Class<?> actualTypeArgument, String massageCode) {
        messageTypeToMessageClass.put(massageCode, actualTypeArgument);
        massageClassToCode.put(actualTypeArgument, massageCode);
        processedCalsesSet.add(actualTypeArgument);
    }

    @Override
    public Class<?> getMessageTypeByCode(String messageCode) {
        Class<?> messageClass = messageTypeToMessageClass.get(messageCode);
        if (messageClass == null) {
            synchronized (messageTypeToMessageClass) {
                messageClass = messageTypeToMessageClass.get(messageCode);
                if (messageClass == null) {
                    Set<Class<?>> typesToLookFor = getConfig().getTypesAnnotatedWith(NetworkDto.class).stream()
                            .filter(type -> !processedCalsesSet.contains(type))
                            .collect(Collectors.toSet());
                    for (Class<?> clazz : typesToLookFor) {
                        String massageCodeFromAnnotation = clazz.getAnnotation(NetworkDto.class).massageCode();
                        putTcpMessageToCash(clazz, massageCodeFromAnnotation);
                        if (messageCode.equals(massageCodeFromAnnotation)) {
                            return clazz;
                        }
                    }
                } else {
                    return Error404Dto.class;
                }
            }
        }
        return messageClass;
    }

    @Override
    public String getMessageCodeByType(Class<?> messageType) {
        String messageCode = massageClassToCode.get(messageType);
        if (messageCode == null) {
            synchronized (massageClassToCode) {
                messageCode = massageClassToCode.get(messageType);
                if (messageCode == null) {
                    messageCode = messageType.getAnnotation(NetworkDto.class).massageCode();
                    putTcpMessageToCash(messageType, messageCode);
                }
            }
        }
        return messageCode;
    }

    public void setFactory(ObjectFactory factory) {
        this.factory = factory;
    }

    public Config getConfig() {
        if (this.config == null) {
            synchronized (this) {
                if (this.config == null) {
                    this.config = new JavaConfig(getPropertyValue("infrastructure.package.to.annotation.scan"));
                }
            }
        }
        return this.config;
    }

    private <T> void putToObjectsCashIfSingleton(Class<T> type, Class<? extends T> implClass, T instance) {
        if (implClass.isAnnotationPresent(Singleton.class)) {
            objectsCash.put(type, instance);
        }
    }
}
