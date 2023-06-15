package testingFunctional.crud.dao;

import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.dal.conection.dao.JDBCAbstractGenericDao;
import infrastructure.dal.conection.dao.ResultSetToEntityMapper;
import testingFunctional.crud.model.UserModel;

import java.util.Optional;

@Singleton
@NeedConfig
public class UserDaoImpl extends JDBCAbstractGenericDao<UserModel> implements UserDao {

    public static final String GET_USER_BY_ID = "get.user.by.id";
    public static final String ID = "id";

    @Override
    public Optional<UserModel> getUserById(int parameter) {
        return findById(parameter, getDbRequestsString(GET_USER_BY_ID), getResultSetToUserMapper());
    }

    private ResultSetToEntityMapper<UserModel> getResultSetToUserMapper() {
        return resultSet -> {
            UserModel userModel = new UserModel();
            userModel.setId(resultSet.getInt(ID));
            return userModel;
        };
    }
}
