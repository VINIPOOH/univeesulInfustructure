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
}
