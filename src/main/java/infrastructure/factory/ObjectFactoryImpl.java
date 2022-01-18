package infrastructure.factory;

import infrastructure.ApplicationContext;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.PostConstruct;
import infrastructure.exception.ReflectionException;
import infrastructure.factory.сonfigurator.obj.ObjectConfigurator;
import infrastructure.factory.сonfigurator.proxy.ProxyConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates beans
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ObjectFactoryImpl implements ObjectFactory {
    private static final Logger log = LogManager.getLogger(ObjectFactoryImpl.class);

    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators = new ArrayList<>();
    private final List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    public ObjectFactoryImpl(ApplicationContext context) {
        log.debug("");

        this.context = context;

        try {
            for (Class<? extends ObjectConfigurator> aClass : context.getConfig().getSubTypesOf(ObjectConfigurator.class)) {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            }
            for (Class<? extends ProxyConfigurator> aClass : context.getConfig().getSubTypesOf(ProxyConfigurator.class)) {
                proxyConfigurators.add(aClass.getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
    }

    @Override
    public <T> T createObject(Class<T> implClassKey) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.debug("");

        T toReturn = create(implClassKey);
        configure(toReturn);
        invokeInit(implClassKey, toReturn);
        toReturn = wrapWithProxyIfNeeded(implClassKey, toReturn);
        return toReturn;
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }

    private <T> void configure(T instance) {
        Class currentClass = instance.getClass();
        while (currentClass.isAnnotationPresent(NeedConfig.class)) {
            Class finalCurrentClass = currentClass;
            configurators.forEach(objectConfigurator -> objectConfigurator.configure(instance, finalCurrentClass, context));
            currentClass = currentClass.getSuperclass();
        }
    }

    private <T> void invokeInit(Class<T> implClass, T instance) {
        Arrays.stream(implClass.getMethods()).filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    try {
                        method.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ReflectionException();
                    }
                });
    }

    private <T> T wrapWithProxyIfNeeded(Class<T> implClass, T instance) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            instance = (T) proxyConfigurator.replaceWithProxyIfNeeded(instance, implClass, context);
        }
        return instance;
    }
}




