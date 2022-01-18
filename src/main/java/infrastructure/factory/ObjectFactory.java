package infrastructure.factory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ObjectFactory {

    <T> T createObject(Class<T> implClassKey) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}




