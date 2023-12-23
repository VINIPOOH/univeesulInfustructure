package dal.dao.impl;

import dal.dao.UserDao;
import dal.entity.RoleType;
import dal.entity.User;
import dal.exeption.AskedDataIsNotCorrect;
import dal.exeption.DBRuntimeException;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.dal.conection.ConnectionProxy;
import infrastructure.dal.conection.dao.JDBCAbstractGenericDao;
import infrastructure.dal.conection.dao.ResultSetToEntityMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implements an interface for work with {@link User}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class JDBCUserDao extends JDBCAbstractGenericDao<User> implements UserDao {
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ACCOUNT_NON_EXPIRED = "account_non_expired";
    private static final String ACCOUNT_NON_LOCKED = "account_non_locked";
    private static final String CREDENTIALS_NON_EXPIRED = "credentials_non_expired";
    private static final String ENABLED = "enabled";
    private static final String USER_MONEY_IN_CENTS = "user_money_in_cents";
    private static final String ROLE = "role";
    private static final String USER_FIND_BY_EMAIL = "user.find.by.email";
    private static final String USER_REPLENISH_BALANCE = "user.replenish.balance";
    private static final String USER_SAVE = "user.save";
    private static final String GET_USER_BALANCE_IF_ENOGFE_MONEY =
            "user.get.user.balance.if.enough.money";
    private static final String GET_USER_BALANCE_BY_ID = "user.get.balance.by.id";
    private static final String GET_ALL_USERS_INFO = "get.all.users.info";
    private static final Logger log = LogManager.getLogger(JDBCUserDao.class);

    @Override
    public Optional<User> findByEmailAndPasswordWithPermissions(String email, String password) {
        log.debug("findByEmailAndPasswordWithPermissions");

        ResultSetToEntityMapper<User> mapper = getUserResultSetToEntityMapper();

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(USER_FIND_BY_EMAIL))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            Optional<User> user;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                user = resultSet.next() ? Optional.of(mapper.map(resultSet)) : Optional.empty();
            }
            return user;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    /**
     * @throws AskedDataIsNotCorrect if there no user with such id
     */
    @Override
    public boolean replenishUserBalance(long userId, long money) throws AskedDataIsNotCorrect {
        log.debug("replenishUserBalance");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(USER_REPLENISH_BALANCE))) {
            preparedStatement.setLong(1, money);
            preparedStatement.setLong(2, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new AskedDataIsNotCorrect();
        }
    }

    /**
     * @throws AskedDataIsNotCorrect if that email already taken
     */
    @Override
    public boolean save(String email, String password) throws AskedDataIsNotCorrect {
        log.debug("save");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(USER_SAVE))) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new AskedDataIsNotCorrect();
        }
    }

    @Override
    public boolean withdrawUserBalanceOnSumIfItPossible(long userId, long sumWhichUserNeed) throws SQLException {
        log.debug("replenishUserBalanceOnSumIfItPossible");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(GET_USER_BALANCE_IF_ENOGFE_MONEY))) {
            preparedStatement.setLong(1, sumWhichUserNeed);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, sumWhichUserNeed);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    /**
     * @throws AskedDataIsNotCorrect if there is no users with such id
     */
    @Override
    public long getUserBalanceByUserID(long userId) throws AskedDataIsNotCorrect {
        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(GET_USER_BALANCE_BY_ID))) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(USER_MONEY_IN_CENTS);
                }
                throw new AskedDataIsNotCorrect();
            }
        } catch (SQLException e) {
            log.error("SQLException", e);

            throw new AskedDataIsNotCorrect();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(GET_ALL_USERS_INFO))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<User> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(User.builder()
                            .email(resultSet.getString(EMAIL))
                            .password(resultSet.getString(PASSWORD))
                            .roleType(RoleType.valueOf(resultSet.getString(ROLE)))
                            .build());
                }
                return result;
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<User> getUserResultSetToEntityMapper() {
        return resultSet -> User.builder()
                .id(resultSet.getLong(ID))
                .email(resultSet.getString(EMAIL))
                .password(resultSet.getString(PASSWORD))
                .accountNonExpired(resultSet.getBoolean(ACCOUNT_NON_EXPIRED))
                .accountNonLocked(resultSet.getBoolean(ACCOUNT_NON_LOCKED))
                .credentialsNonExpired(resultSet.getBoolean(CREDENTIALS_NON_EXPIRED))
                .enabled(resultSet.getBoolean(ENABLED))
                .userMoneyInCents(resultSet.getLong(USER_MONEY_IN_CENTS))
                .roleType(RoleType.valueOf(resultSet.getString(ROLE)))
                .build();
    }
}
