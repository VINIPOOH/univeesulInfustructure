package infrastructure.factory.configurator.proxy;

import infrastructure.dal.conection.pool.ConnectionManager;
import infrastructure.ApplicationContext;
import infrastructure.anotation.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionProxyConfiguratorTest {
    @InjectMocks
    TransactionProxyConfigurator transactionProxyConfigurator;

    @Mock
    ConnectionManager connectionManager;
    @Mock
    ApplicationContext applicationContext;

    @Test
    public void replaceWithProxyIfNeededWithInterfaceAllCorrect() throws Throwable {

        class TestClass implements TestInterFace {
            @Transaction
            public boolean testVoid() {
                return true;
            }
        }
        TestClass testClass = new TestClass();
        when(applicationContext.getObject(any(Class.class))).thenReturn(connectionManager);

        boolean result = ((TestInterFace) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).testVoid();


        verify(applicationContext, times(1)).getObject(any(Class.class));
        verify(connectionManager, times(1)).startTransaction();
        verify(connectionManager, times(1)).commit();
        verify(connectionManager, times(1)).close();
        verify(connectionManager, times(0)).rollBack();
        assertTrue(result);
    }

    @Test
    public void replaceWithProxyIfNeededWithInterfaceAllCorrectInClassNoTransactionVoids() throws Throwable {
        class TestClass implements TestInterFace {
            public boolean testVoid() {
                return true;
            }
        }
        TestClass testClass = new TestClass();

        boolean result = ((TestInterFace) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).testVoid();


        verify(applicationContext, times(0)).getObject(any(Class.class));
        verify(connectionManager, times(0)).startTransaction();
        verify(connectionManager, times(0)).commit();
        verify(connectionManager, times(0)).close();
        verify(connectionManager, times(0)).rollBack();
        assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void replaceWithProxyIfNeededWithInterfaceThrowUncheckedException() throws Throwable {

        class TestClass implements TestInterFace {
            @Transaction
            public boolean testVoid() {
                throw new RuntimeException();
            }
        }
        TestClass testClass = new TestClass();
        when(applicationContext.getObject(any(Class.class))).thenReturn(connectionManager);

        ((TestInterFace) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).testVoid();

        fail();
    }

    @Test(expected = Throwable.class)
    public void replaceWithProxyIfNeededWithInterfaceThrowCheckedException() throws Throwable {

        class TestClass implements TestInterFace {
            @Transaction
            public boolean testVoid() throws Throwable {
                throw new Throwable();
            }
        }
        TestClass testClass = new TestClass();
        when(applicationContext.getObject(any(Class.class))).thenReturn(connectionManager);

        ((TestInterFace) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).testVoid();

        fail();
    }

    @Test
    public void replaceWithProxyIfNeededWhereNoInterfaceAllCorrect() throws Throwable {
        TestClass testClass = new TestClass();
        when(applicationContext.getObject(any(Class.class))).thenReturn(connectionManager);

        boolean result = ((TestClass) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).testVoid();


        verify(applicationContext, times(1)).getObject(any(Class.class));
        verify(connectionManager, times(1)).startTransaction();
        verify(connectionManager, times(1)).commit();
        verify(connectionManager, times(1)).close();
        verify(connectionManager, times(0)).rollBack();
        assertTrue(result);
    }

    @Test
    public void replaceWithProxyIfNeededWhereNoInterfaceAllCorrectInClassNoTransactionVoids() throws Throwable {
        TestClass testClass = new TestClass();

        boolean result = ((TestClass) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).noTransactionTestVoid();

        verify(applicationContext, times(0)).getObject(any(Class.class));
        verify(connectionManager, times(0)).startTransaction();
        verify(connectionManager, times(0)).commit();
        verify(connectionManager, times(0)).close();
        verify(connectionManager, times(0)).rollBack();
        assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void replaceWithProxyIfNeededWhereNoInterfaceThrowUncheckedException() throws Throwable {
        TestClassWhichThrowUncheckedException testClass = new TestClassWhichThrowUncheckedException();

        ((TestClassWhichThrowUncheckedException) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClass.class, applicationContext)).testVoid();

        fail();
    }

    @Test(expected = Throwable.class)
    public void replaceWithProxyIfNeededWhereNoInterfaceThrowCheckedException() throws Throwable {
        TestClass testClass = new TestClass();
        when(applicationContext.getObject(any(Class.class))).thenReturn(connectionManager);

        ((TestClassWhichThrowCheckedException) transactionProxyConfigurator.replaceWithProxyIfNeeded(testClass, TestClassWhichThrowCheckedException.class, applicationContext)).testVoid();

        fail();
    }

    interface TestInterFace {
        boolean testVoid() throws Throwable;
    }

    static class TestClass {
        @Transaction
        public boolean testVoid() {
            return true;
        }

        public boolean noTransactionTestVoid() {
            return true;
        }
    }

    static class TestClassWhichThrowUncheckedException {
        @Transaction
        public boolean testVoid() {
            throw new RuntimeException();
        }
    }

    static class TestClassWhichThrowCheckedException {
        @Transaction
        public boolean testVoid() throws Throwable {
            throw new Throwable();
        }
    }


}
