package infrastructure.сonfig;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Declare interface for represent application configuration info
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> ifc);

    Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation);

    <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type);

}
