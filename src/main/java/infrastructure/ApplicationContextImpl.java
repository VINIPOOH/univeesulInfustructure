package infrastructure;

import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import infrastructure.controller.MultipleMethodController;
import infrastructure.controller.PhantomController;
import infrastructure.currency.CurrencyInfo;
import infrastructure.currency.CurrencyInfoLoader;
import infrastructure.exception.ReflectionException;
import infrastructure.factory.ObjectFactory;
import infrastructure.сonfig.Config;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Represents info about context in which application run.
 * Contains info about currency rates.
 * Controls beans and commands.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ApplicationContextImpl implements ApplicationContext{
    private static final Logger log = LogManager.getLogger(ApplicationContextImpl.class);
    private final Map<Class, Object> objectsCash;
    private final Map<String, MultipleMethodController> controllerMap;
    private final Map<String, CurrencyInfo> currencies;
    private final Class defaultEndpoint = PhantomController.class;
    private final Config config;
    private ObjectFactory factory;

    /**
     *
     * @param config config instance
     * @param preparedCash preload objects map. May be used for load classes which have more then one implementation
     * @param controllersPrepared preload controllers map.
     * @param currencyInfoLoader used in constructor to load into {@link ApplicationContextImpl#currencies} exchange rates
     */
    public ApplicationContextImpl(Config config, Map<Class, Object> preparedCash,
                                  Map<String, MultipleMethodController> controllersPrepared,
                                  CurrencyInfoLoader currencyInfoLoader) {
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


    public MultipleMethodController getCommand(String linkKey) {
        log.debug("");

        if (controllerMap.containsKey(linkKey)) {
            return controllerMap.get(linkKey);
        }
        synchronized (controllerMap) {
            if (controllerMap.containsKey(linkKey)) {
                return controllerMap.get(linkKey);
            }
            for (Class<?> clazz : config.getTypesAnnotatedWith(Endpoint.class)) {
                Endpoint annotation = clazz.getAnnotation(Endpoint.class);
                for (String i : annotation.value()) {
                    if (i.equals(linkKey)) {
                        MultipleMethodController toReturn = (MultipleMethodController) getObject(clazz);
                        putToCommandMapIfSingleton(linkKey, clazz, toReturn);
                        return toReturn;
                    }
                }
            }
        }
        return (MultipleMethodController) getObject(defaultEndpoint);
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

    private void putToCommandMapIfSingleton(String link, Class<?> clazz, MultipleMethodController toReturn) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            controllerMap.put(link, toReturn);
        }
    }
}
