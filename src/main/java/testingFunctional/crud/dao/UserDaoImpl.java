package testingFunctional.crud.dao;

import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.dal.conection.dao.EntityToPreparedStatementMapper;
import infrastructure.dal.conection.dao.JDBCAbstractGenericDao;
import infrastructure.dal.conection.dao.ResultSetToEntityMapper;
import testingFunctional.crud.model.UserModel;

import java.util.List;
import java.util.Optional;

@Singleton
@NeedConfig
public class UserDaoImpl extends JDBCAbstractGenericDao<UserModel> implements UserDao {


    public static final String GET_USER_BY_ID = "get.user.by.id";
    public static final String GET_ALL_USER_BY_ID = "get.all.user.by.id";
    public static final String CREATE_USER = "create.user.by.id";
    public static final String UPDATE_USER_ID = "update.user.by.id";
    public static final String DELETE_USER_ID = "delete.user.by.id";
    public static final String ID = "id";

    @Override
    public Optional<UserModel> getUserById(int parameter) {
        return findById(parameter, getDbRequestsString(GET_USER_BY_ID), getResultSetToUserMapper());
    }

    @Override
    public boolean createUser(UserModel userModel) {
        return create(userModel, getDbRequestsString(CREATE_USER), getUserModelEntityToPreparedStatementCreateMapper());
    }

    @Override
    public boolean updateUser(UserModel userModel) {
        return update(userModel, getDbRequestsString(UPDATE_USER_ID), getModelEntityToPreparedStatementMapper());
    }

    @Override
    public boolean deleteUser(UserModel userModel) {
        return delete(userModel, getDbRequestsString(DELETE_USER_ID), getEntityToPreparedStatementDeleteMapper());
    }

    @Override
    public List<UserModel> getAllUsers() {
        return findAll(getDbRequestsString(GET_ALL_USER_BY_ID), getResultSetToUserMapper());
    }

    private static EntityToPreparedStatementMapper<UserModel> getEntityToPreparedStatementDeleteMapper() {
        return (entity, preparedStatement) -> preparedStatement.setInt(1, entity.getId());
    }

    private static EntityToPreparedStatementMapper<UserModel> getModelEntityToPreparedStatementMapper() {
        return (entity, preparedStatement) -> {
            preparedStatement.setInt(2, entity.getId());
            preparedStatement.setString(1, entity.getName());
        };
    }

    private static EntityToPreparedStatementMapper<UserModel> getUserModelEntityToPreparedStatementCreateMapper() {
        return (entity, preparedStatement) -> preparedStatement.setInt(1, entity.getId());
    }

    private ResultSetToEntityMapper<UserModel> getResultSetToUserMapper() {
        return resultSet -> {
            UserModel userModel = new UserModel();
            userModel.setId(resultSet.getInt(ID));
            return userModel;
        };
    }
}
