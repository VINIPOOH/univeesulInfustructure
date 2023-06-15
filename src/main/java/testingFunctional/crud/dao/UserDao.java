package testingFunctional.crud.dao;

import testingFunctional.crud.model.UserModel;

import java.util.Optional;

public interface UserDao {
    Optional<UserModel> getUserById(int parameter);
}
