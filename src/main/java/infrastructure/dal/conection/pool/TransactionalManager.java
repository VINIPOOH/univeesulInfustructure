package infrastructure.dal.conection.pool;

import infrastructure.dal.conection.ConnectionProxy;

import java.sql.SQLException;

/**
 * Manage transactions.
 * Encapsulate the functionality necessary for working with connections in the autocommit=false mode.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface TransactionalManager extends AutoCloseable {
    ConnectionProxy getConnection() throws SQLException;

    void startTransaction() throws SQLException;

    void commit() throws SQLException;

    void rollBack() throws SQLException;

    @Override
    void close();
}
