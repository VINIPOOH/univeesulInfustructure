package logiclayer.service.impl;


import dal.dao.UserDao;
import dal.entity.User;
import dal.exeption.AskedDataIsNotCorrect;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import dto.UserStatisticDto;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.anotation.Transaction;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.exeption.OccupiedLoginException;
import logiclayer.exeption.ToMachMoneyException;
import logiclayer.service.PasswordEncoderService;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.exception.OnClientSideProblemException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implements an interface for work with users
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class UserServiceImpl implements UserService {
    @InjectByType
    private PasswordEncoderService passwordEncoderService;
    @InjectByType
    private UserDao userDao;


    @Override
    public User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException {
        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }

    @Override
    public boolean addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {

        try {
            return userDao.save(registrationInfoDto.getUsername(), passwordEncoderService.encode(registrationInfoDto.getPassword()));
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {

            throw new OccupiedLoginException();
        }

    }

    /**
     * @throws ToMachMoneyException if after replenish will be overload long
     */
    @Override
    @Transaction
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, ToMachMoneyException {

        try {
            if (userDao.getUserBalanceByUserID(userId) + amountMoney <= 0) {
                throw new ToMachMoneyException();
            }
            userDao.replenishUserBalance(userId, amountMoney);
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            throw new NoSuchUserException();
        }
    }

    @Override
    public long getUserBalance(long userId) {
        try {
            return userDao.getUserBalanceByUserID(userId);
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            throw new OnClientSideProblemException();
        }
    }

    @Override
    public List<UserStatisticDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(getUserUserStatisticDtoMapper())
                .collect(Collectors.toList());
    }

    private Function<User, UserStatisticDto> getUserUserStatisticDtoMapper() {
        return user -> UserStatisticDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roleType(user.getRoleType().name())
                .build();
    }
}
