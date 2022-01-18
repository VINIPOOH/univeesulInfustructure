package infrastructure.factory.сonfigurator.obj;

import infrastructure.ApplicationContext;

/**
 * Declare interface for configuration class.
 * If you need add own configurator to chain tuning object. You must implement this interface
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ObjectConfigurator {
    /**
     * @param instance object which mast be configured
     * @param instanceType type of that object
     * @param context give opportunity configure object based on application context
     */
    void configure(Object instance, Class instanceType, ApplicationContext context);
}
