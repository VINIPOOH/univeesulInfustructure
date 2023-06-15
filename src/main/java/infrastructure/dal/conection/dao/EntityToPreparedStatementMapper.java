package infrastructure.dal.conection.dao;

import java.sql.PreparedStatement;

/**
 * Declare method for mapping an entity class into {@link PreparedStatement}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@FunctionalInterface
public interface EntityToPreparedStatementMapper<E> {
    void map(E entity, PreparedStatement preparedStatement);
}
