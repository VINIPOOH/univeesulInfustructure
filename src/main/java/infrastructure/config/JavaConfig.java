package infrastructure.config;

import infrastructure.exception.ConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

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
    public <T> Set<Class<? extends T>> getImplClasses(Class<T> ifc) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
        return classes;
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return scanner.getTypesAnnotatedWith(annotation, false);
    }

    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return scanner.getSubTypesOf(type);
    }

    @Override
    public List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to traverse a type hierarchy in order to process methods from super types
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    Annotation annotInstance = method.getAnnotation(annotation);
                    // TODO process annotInstance
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }

    @Override
    public Method getMethodAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
        return getMethodsAnnotatedWith(type, annotation).get(0); //todo make on failure return null
    }
}












