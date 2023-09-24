package infrastructure.dal.conection.pool.impl;

import infrastructure.dal.conection.ConnectionProxy;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionalConnectionPoolImplTest {

    @InjectMocks
    TransactionalConnectionPoolImpl transactionalConnectionPool;

    @Mock
    BasicDataSource basicDataSource;

    @Spy
    Connection connection;

    @Test
    public void init() throws NoSuchFieldException, IllegalAccessException {
        setFieldValueByItName("dbUrl", "1");
        setFieldValueByItName("dbUser", "1");
        setFieldValueByItName("dbPassword", "1");
        setFieldValueByItName("dbDriver", "1");
        setFieldValueByItName("dbMinIdle", "1");
        setFieldValueByItName("dbMaxIdle", "1");
        setFieldValueByItName("dbInitialSize", "1");
        setFieldValueByItName("dbMaxOpenStatement", "1");

        transactionalConnectionPool.init();

        verify(basicDataSource, times(1)).setUrl(anyString());
        verify(basicDataSource, times(1)).setUsername(anyString());
        verify(basicDataSource, times(1)).setPassword(anyString());
        verify(basicDataSource, times(1)).setDriverClassName(anyString());
        verify(basicDataSource, times(1)).setMinIdle(anyInt());
        verify(basicDataSource, times(1)).setMaxIdle(anyInt());
        verify(basicDataSource, times(1)).setInitialSize(anyInt());
        verify(basicDataSource, times(1)).setMaxOpenPreparedStatements(anyInt());
    }

    @Test
    public void getConnectionAdapter() throws SQLException {
        when(basicDataSource.getConnection()).thenReturn(connection);

        ConnectionProxy result = transactionalConnectionPool.getConnectionAdapter();

        assertEquals(connection, result.getConnection());
    }

    @Test(expected = SQLException.class)
    public void getConnectionAdapterProblemsWithDB() throws SQLException {
        when(basicDataSource.getConnection()).thenThrow(SQLException.class);

        transactionalConnectionPool.getConnectionAdapter();

        fail();
    }

    @Test
    public void getConnectionAdapterPreparedForTransaction() throws SQLException, NoSuchFieldException, IllegalAccessException {
        when(basicDataSource.getConnection()).thenReturn(connection);

        ConnectionProxy result = transactionalConnectionPool.getConnectionAdapterPreparedForTransaction();
        Field isTransactional = result.getClass().getDeclaredField("isTransaction");

        isTransactional.setAccessible(true);
        assertTrue(isTransactional.getBoolean(result));
        assertEquals(connection, result.getConnection());
    }

    @Test(expected = SQLException.class)
    public void getConnectionAdapterPreparedForTransactionProblemsWithDB() throws SQLException, NoSuchFieldException, IllegalAccessException {
        when(basicDataSource.getConnection()).thenThrow(SQLException.class);

        transactionalConnectionPool.getConnectionAdapterPreparedForTransaction();

        fail();
    }

    private void setFieldValueByItName(String fieldName, String fieldValue) throws IllegalAccessException, NoSuchFieldException {
        Field field = transactionalConnectionPool.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(transactionalConnectionPool, fieldValue);
    }

}