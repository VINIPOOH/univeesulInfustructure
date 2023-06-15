package infrastructure.dal.conection.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetToEntityMapper<E> {

    /**
     * No need to use resultSet.next method thins it was used in AbstractGenericDao
     * @param resultSet
     * @return
     * @throws SQLException
     */
    E map(ResultSet resultSet) throws SQLException;
}
