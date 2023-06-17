package infrastructure.dal.conection.dao;

import java.util.List;
import java.util.Optional;

/**
 * Declares widely used methods in DAO classes.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface AbstractGenericDao<E> {

    List<E> findAllByLongParam(long param, String query, ResultSetToEntityMapper<E> mapper);

    String getDbRequestsString(String getUserById);

    List<E> findAllByLongParamPageable(long param, Integer offset, Integer limit, String query, ResultSetToEntityMapper<E> mapper);

    long countAllByLongParam(long param, String query);

    boolean create(E entity, String saveQuery, EntityToPreparedStatementMapper<E> mapper);

    Optional<E> findById(long id, String query, ResultSetToEntityMapper<E> mapper);

    boolean delete(E entity, String query, EntityToPreparedStatementMapper<E> mapper);

    List<E> findAll(String query, ResultSetToEntityMapper<E> mapper);

    boolean update(E entity, String saveQuery, EntityToPreparedStatementMapper<E> mapper);
}
