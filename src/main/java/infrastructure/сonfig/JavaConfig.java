package infrastructure.сonfig;

import infrastructure.exception.ConfigurationException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Represent application configuration info from java code
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class JavaConfig implements Config {

    private final Reflections scanner;

    public JavaConfig(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
        if (classes.size() != 1) {
            throw new ConfigurationException(ifc + " has 0 or more than one impl please update your config");
        }
        return classes.iterator().next();
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return scanner.getTypesAnnotatedWith(annotation, false);
    }

    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return scanner.getSubTypesOf(type);
    }
}












