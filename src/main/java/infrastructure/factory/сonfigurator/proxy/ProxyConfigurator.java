package infrastructure.factory.сonfigurator.proxy;

import infrastructure.ApplicationContext;

/**
 * Declare interface for proxying class.
 * If you need add own proxy configurator to chain tuning object. You must implement this interface
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ProxyConfigurator {
    /**
     * @param instance object which mast be configured
     * @param instanceType type of that object
     * @param context give opportunity configure object based on application context
     */
    Object replaceWithProxyIfNeeded(Object instance, Class instanceType, ApplicationContext context);
}
