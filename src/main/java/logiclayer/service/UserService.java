package logiclayer.service;

import dal.entity.User;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import dto.UserStatisticDto;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.exeption.OccupiedLoginException;
import logiclayer.exeption.ToMachMoneyException;

import java.util.List;

/**
 * Declares an interface for work with users
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface UserService {


    User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException;

    boolean addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;
    /**
     * @throws ToMachMoneyException if after replenish will be overload long
     */
    void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, ToMachMoneyException;

    long getUserBalance(long userId);

    List<UserStatisticDto> getAllUsers();

}
