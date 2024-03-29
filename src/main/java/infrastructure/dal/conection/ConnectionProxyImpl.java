package infrastructure.dal.conection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Proxy on {@link Connection}. It is necessary to prevent the connection from being closed if the connection has
 * status autocommit=falls
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ConnectionProxyImpl implements ConnectionProxy {
    private static final Logger log = LogManager.getLogger(ConnectionProxyImpl.class);

    private final Connection connection;
    private boolean isTransaction = false;

    public ConnectionProxyImpl(Connection connection) {
        log.debug("created");

        this.connection = connection;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        log.debug("prepareStatement");


        return connection.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        log.debug("prepareStatement");

        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * close only if isTransaction==false
     *
     * @throws SQLException
     */
    @Override
    public void close() throws SQLException {
        log.debug("close");

        if (!isTransaction) {
            connection.close();
        }
    }

    @Override
    public void setAutoCommit(boolean isAutoCommit) throws SQLException {
        log.debug("setAutoCommit");

        connection.setAutoCommit(isAutoCommit);
    }

    @Override
    public Connection getConnection() {
        log.debug("getSubject");

        return connection;
    }

    @Override
    public void rollBack() throws SQLException {
        log.debug("rollBack");

        connection.rollback();
    }

    @Override
    public void commit() throws SQLException {
        log.debug("commit");

        connection.commit();
    }

    @Override
    public void setIsTransaction(boolean isTransaction) {
        log.debug("setIsTransaction");

        this.isTransaction = isTransaction;
    }
}
