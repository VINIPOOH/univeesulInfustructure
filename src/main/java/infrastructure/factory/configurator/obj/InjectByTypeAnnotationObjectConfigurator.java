package infrastructure.factory.configurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.exception.ReflectionException;

import java.lang.reflect.Field;

/**
 * Inject in configurable object fields properties marked annotation {@link InjectByType}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    public void configure(Object instance, Class instanceType, ApplicationContext context) {

        for (Field field : instanceType.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                field.setAccessible(true);
                Object object = context.getObject(field.getType());
                try {
                    field.set(instance, object);
                } catch (IllegalAccessException e) {
                    throw new ReflectionException("impossible we made accessible true");
                }
            }
        }

    }
}
