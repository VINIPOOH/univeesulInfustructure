package dal.dao;

import dal.entity.User;
import dal.exeption.AskedDataIsNotCorrect;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Declares an interface for work with {@link User}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface UserDao {

    Optional<User> findByEmailAndPasswordWithPermissions(String email, String password);
    /**
     * @throws AskedDataIsNotCorrect if that email already taken
     */
    boolean save(String email, String password) throws AskedDataIsNotCorrect;
    /**
     * @throws AskedDataIsNotCorrect if there no user with such id
     */
    boolean replenishUserBalance(long userId, long amountMoney) throws AskedDataIsNotCorrect;

    boolean withdrawUserBalanceOnSumIfItPossible(long userId, long sumWhichUserNeed) throws SQLException;
    /**
     * @throws AskedDataIsNotCorrect if there is no users with such id
     */
    long getUserBalanceByUserID(long userId) throws AskedDataIsNotCorrect;

    List<User> getAllUsers();
}
