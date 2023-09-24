package infrastructure.factory.configurator.proxy;

import infrastructure.ApplicationContext;
import infrastructure.anotation.Transaction;
import infrastructure.dal.conection.pool.ConnectionManager;
import net.sf.cglib.proxy.Enhancer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

/**
 * Create proxy for to provide transactional work to methods marked annotation {@link Transaction}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class TransactionProxyConfigurator implements ProxyConfigurator {
    private static final Logger log = LogManager.getLogger(TransactionProxyConfigurator.class);

    @Override
    public Object replaceWithProxyIfNeeded(Object instance, Class instanceType, ApplicationContext context) {

        for (Method method : instanceType.getMethods()) {
            if (method.isAnnotationPresent(Transaction.class)) {
                if (instanceType.getInterfaces().length == 0) {
                    return Enhancer.create(instanceType, new net.sf.cglib.proxy.InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            return getInvocationHandlerLogic(method, args, instance, context);
                        }
                    });
                }
                return Proxy.newProxyInstance(instanceType.getClassLoader(), instanceType.getInterfaces(),
                        (proxy, met, args) -> getInvocationHandlerLogic(met, args, instance, context));
            }
        }
        return instance;
    }

    private Object getInvocationHandlerLogic(Method method, Object[] args, Object t, ApplicationContext context) throws Throwable {
        log.debug("getInvocationHandlerLogic");
        try {
            if (t.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Transaction.class)) {
                return doTransactionMethodCall(method, args, t, context.getObject(ConnectionManager.class));
            }
        } catch (NoSuchMethodException | SQLException | InstantiationException ex) {
            log.debug(ex);
        }
        try {
            return method.invoke(t, args);
        } catch (Throwable e) {
            throw e;
        }
    }

    private Object doTransactionMethodCall(Method method, Object[] args, Object t, ConnectionManager connectionManager)
            throws Throwable {
        try {
            connectionManager.startTransaction();
            Object result = method.invoke(t, args);
            connectionManager.commit();
            connectionManager.close();
            return result;
        } catch (Throwable e) {
            connectionManager.rollBack();
            connectionManager.close();
            throw e;
        }
    }
}
