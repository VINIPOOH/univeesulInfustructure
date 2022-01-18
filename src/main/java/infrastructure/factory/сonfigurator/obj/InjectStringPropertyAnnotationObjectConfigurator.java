package infrastructure.factory.сonfigurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectStringProperty;
import infrastructure.exception.ReflectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

/**
 * Inject in configurable string properties marked annotation {@link InjectStringProperty}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class InjectStringPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static final Logger log = LogManager.getLogger(InjectStringPropertyAnnotationObjectConfigurator.class);

    private final ResourceBundle resourceBundle;


    public InjectStringPropertyAnnotationObjectConfigurator() {
        log.debug("");

        resourceBundle = ResourceBundle.getBundle("application");
    }

    @Override
    public void configure(Object instance, Class instanceType, ApplicationContext context) {
        for (Field field : instanceType.getDeclaredFields()) {
            InjectStringProperty annotation = field.getAnnotation(InjectStringProperty.class);
            if (annotation != null) {
                String value = annotation.value().isEmpty() ? resourceBundle.getString(field.getName()) : resourceBundle.getString(annotation.value());
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
