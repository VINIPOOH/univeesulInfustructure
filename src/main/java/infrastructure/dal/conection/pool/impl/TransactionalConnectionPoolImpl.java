package infrastructure.dal.conection.pool.impl;

import infrastructure.anotation.*;
import infrastructure.dal.conection.ConnectionProxy;
import infrastructure.dal.conection.ConnectionProxyImpl;
import infrastructure.dal.conection.pool.TransactionalConnectionPool;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * The database connection pool which configure connections for transaction work with them.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class TransactionalConnectionPoolImpl implements TransactionalConnectionPool {
    private static final Logger log = LogManager.getLogger(TransactionalConnectionPoolImpl.class);

    @InjectStringProperty
    private String dbUrl;
    @InjectStringProperty
    private String dbUser;
    @InjectStringProperty
    private String dbPassword;
    @InjectStringProperty
    private String dbDriver;
    @InjectStringProperty
    private String dbMinIdle;
    @InjectStringProperty
    private String dbMaxIdle;
    @InjectStringProperty
    private String dbInitialSize;
    @InjectStringProperty("db.maxOpenStatement")
    private String dbMaxOpenStatement;
    @InjectByType
    private BasicDataSource ds;

    @PostConstruct
    public void init() {
        log.debug("created");

        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword((dbPassword));
        ds.setDriverClassName(dbDriver);
        ds.setMinIdle(Integer.parseInt(dbMinIdle));
        ds.setMaxIdle(Integer.parseInt(dbMaxIdle));
        ds.setInitialSize(Integer.parseInt(dbInitialSize));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(dbMaxOpenStatement));
    }

    @Override
    public ConnectionProxy getConnectionAdapter() throws SQLException {
        return new ConnectionProxyImpl(ds.getConnection());
    }

    /**
     * @return wrapped {@link java.sql.Connection} with auto commit false
     * @throws SQLException
     */
    @Override
    public ConnectionProxy getConnectionAdapterPreparedForTransaction() throws SQLException {
        ConnectionProxy toReturn = new ConnectionProxyImpl(ds.getConnection());
        toReturn.setAutoCommit(false);
        toReturn.setIsTransaction(true);
        return toReturn;
    }
}
