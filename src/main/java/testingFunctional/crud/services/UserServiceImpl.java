package testingFunctional.crud.services;

import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import testingFunctional.crud.dao.UserDao;
import testingFunctional.crud.model.UserModel;

@Singleton
@NeedConfig
public class UserServiceImpl implements UserService {
    @InjectByType
    private UserDao userDao;

    @Override
    public String getUserById(int parameter) {
        UserModel userModel = userDao.getUserById(parameter).get();
        return userModel.toString();
    }

    @Override
    public String createUser(int userId) {
        if (userDao.createUser(new UserModel(userId))) {
            return getUserById(userId);
        }
        return "";
    }

    @Override
    public String updateUserName(int userId, String userName) {
        if (userDao.updateUser(new UserModel(userId, userName))) {
            return getUserById(userId);
        }
        return "";
    }

    @Override
    public String deleteUserById(int userId) {
        userDao.deleteUser(new UserModel(userId));
        return "";
    }

    @Override
    public String getAllUsers() {
        return userDao.getAllUsers().stream().map(UserModel::toString).reduce((s, s2) -> s+","+s2).get();
    }
}
