package infrastructure.dal.conection.pool;

import infrastructure.dal.conection.ConnectionProxy;

import java.sql.SQLException;

/**
 * The database connection pool which configure connections for transaction work with them.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface TransactionalConnectionPool {
    ConnectionProxy getConnectionAdapter() throws SQLException;

    ConnectionProxy getConnectionAdapterPreparedForTransaction() throws SQLException;
}
