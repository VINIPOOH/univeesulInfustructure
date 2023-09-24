package infrastructure.dal.conection.pool.impl;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.dal.conection.ConnectionProxy;
import infrastructure.dal.conection.pool.TransactionalConnectionPool;
import infrastructure.dal.conection.pool.ConnectionManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * The class manages transactions.
 * Encapsulates the functionality necessary for working with connections in the autocommit=false mode.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class ConnectionManagerImpl implements ConnectionManager {

    private static final Logger log = LogManager.getLogger(ConnectionManagerImpl.class);

    @InjectByType
    private
    TransactionalConnectionPool transactionalConnectionPool;


    @InjectByType
    private ThreadLocal<ConnectionProxy> connectionThreadLocal;

    public ConnectionProxy getConnection() throws SQLException {
        log.debug("");

        ConnectionProxy connection = connectionThreadLocal.get();
        if (connection != null) {
            return connection;
        }
        return transactionalConnectionPool.getConnectionAdapter();
    }

    public void startTransaction() throws SQLException {
        log.debug("startTransaction");

        ConnectionProxy connection = connectionThreadLocal.get();
        if (connection != null) {
            throw new SQLException("Transaction already started");
        }
        connectionThreadLocal.set(transactionalConnectionPool.getConnectionAdapterPreparedForTransaction());
    }

    public void commit() throws SQLException {
        log.debug("commit");

        ConnectionProxy connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be commit");
        }
        connection.commit();
    }

    public void rollBack() throws SQLException {
        log.debug("rollBack");

        ConnectionProxy connection = connectionThreadLocal.get();

        if (connection == null) {
            throw new SQLException("Transaction not started to be rollback");
        }

        connection.rollBack();
    }

    public void close() {
        log.debug("close");

        ConnectionProxy connection = connectionThreadLocal.get();

        if (connection == null) {
            log.error("transaction is already closed");
            return;
        }
        connection.setIsTransaction(false);
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("problems with db");
        }
        connectionThreadLocal.remove();
    }

}
