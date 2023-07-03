package infrastructure.factory.configurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectStringProperty;
import infrastructure.exception.ReflectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * Inject in configurable string properties marked annotation {@link InjectStringProperty}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class InjectStringPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static final Logger log = LogManager.getLogger(InjectStringPropertyAnnotationObjectConfigurator.class);

    @Override
    public void configure(Object instance, Class instanceType, ApplicationContext context) {
        for (Field field : instanceType.getDeclaredFields()) {
            InjectStringProperty annotation = field.getAnnotation(InjectStringProperty.class);
            if (annotation != null) {
                String value = annotation.value().isEmpty() ?
                        context.getPropertyValue(field.getName()) :
                        context.getPropertyValue(annotation.value());
                field.setAccessible(true);
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new ReflectionException("impossible we made accessible true");
                }
            }
        }
    }
}
