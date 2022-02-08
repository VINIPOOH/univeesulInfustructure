package infrastructure;

import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.Singleton;
import infrastructure.anotation.TcpEndpoint;
import infrastructure.currency.CurrencyInfo;
import infrastructure.currency.CurrencyInfoFromFileLoader;
import infrastructure.currency.CurrencyInfoLoader;
import infrastructure.exception.ReflectionException;
import infrastructure.factory.ObjectFactory;
import infrastructure.factory.ObjectFactoryImpl;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.http.controller.PhantomController;
import infrastructure.soket.web_socket.controller.TcpController;
import infrastructure.сonfig.Config;
import infrastructure.сonfig.JavaConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<String, CurrencyInfo> currencies;
    private final Map<String, TcpController> messageTypeTcpCommandControllerMap;
    private final Map<String, Class> messageTypeToMessageClass;
    private final Map<Class, String> massageTypeToCode;
    private final Class defaultEndpoint = PhantomController.class;
    private final Config config;
    private ObjectFactory factory;

    private static ApplicationContext singleToneApplicationContext;

    public static ApplicationContext getContext() {
        if (singleToneApplicationContext==null){
            synchronized (ApplicationContext.class){
                if (singleToneApplicationContext==null){
                    Map<Class, Object> paramMap = new ConcurrentHashMap<>();
                    paramMap.put(ResourceBundle.class, ResourceBundle.getBundle("db-request"));
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
     * @param massageTypeToCode
     */
    public ApplicationContextImpl(Config config, Map<Class, Object> preparedCash,
                                  Map<String, MultipleMethodController> controllersPrepared,
                                  CurrencyInfoLoader currencyInfoLoader, Map<String, TcpController> messageTypeTcpCommandControllerMap, Map<String, Class> messageTypeToMessageClass, Map<Class, String> massageTypeToCode) {
        this.messageTypeTcpCommandControllerMap = messageTypeTcpCommandControllerMap;
        this.messageTypeToMessageClass = messageTypeToMessageClass;
        this.massageTypeToCode = massageTypeToCode;
        log.debug("");

        this.controllerMap = controllersPrepared;
        this.config = config;
        this.objectsCash = preparedCash;
        currencies = currencyInfoLoader.getCurrencyInfo();
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
    }

    public CurrencyInfo getCurrencyInfo(String langKey) {
        return currencies.get(langKey);
    }

    @Override
    public <T> void addObject(Class<T> typeKey, Object object){
        if (!objectsCash.containsKey(typeKey)) {
            synchronized (objectsCash){
                if (!objectsCash.containsKey(typeKey)){
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
                throw new ReflectionException();
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
            for (Class<?> clazz : config.getTypesAnnotatedWith(HttpEndpoint.class)) {//todo make consider setting this mup on startup it may slow down upping system but removes necessary to run this code for each Tcp controller
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
    public TcpController getTcpCommandController(String messageType) {
        if (messageTypeTcpCommandControllerMap.containsKey(messageType)) {
            return messageTypeTcpCommandControllerMap.get(messageType);
        }
        synchronized (messageTypeTcpCommandControllerMap) {
            if (messageTypeTcpCommandControllerMap.containsKey(messageType)) {
                return messageTypeTcpCommandControllerMap.get(messageType);
            }
            for (Class<?> clazz : config.getTypesAnnotatedWith(TcpEndpoint.class)) {//todo make consider setting this mup on startup it may slow down upping system but removes necessary to run this code for each Tcp controller
                TcpEndpoint annotation = clazz.getAnnotation(TcpEndpoint.class);
                if (messageType.equals(annotation.requestMessageCode())) {
                    TcpController toReturn = (TcpController) getObject(clazz);
                    putToTcpCommandMapIfSingleton(messageType, clazz, toReturn);
                    messageTypeToMessageClass.put(annotation.requestMessageCode(), annotation.requestMessageType());
                    massageTypeToCode.put(annotation.responseMessageType(), annotation.responseMessageCode());
                    return toReturn;
                }
            }
        }
        return (TcpController) getObject(defaultEndpoint);
    }

    @Override
    public Class getMessageTypeByCode(String messageCode){
        return messageTypeToMessageClass.get(messageCode);
    }

    @Override
    public String getMessageCodeByType(Object messageType){
        return massageTypeToCode.get(messageType);
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
