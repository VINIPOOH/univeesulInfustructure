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
    String RUSSIAN_LANG_COD = "ru";

    List<E> findAllByLongParam(long param, String query, ResultSetToEntityMapper<E> mapper);

    String getDbRequestsString(String getUserById);

    List<E> findAllByLongParamPageable(long param, Integer offset, Integer limit, String query, ResultSetToEntityMapper<E> mapper);

    long countAllByLongParam(long param, String query);

    boolean save(E entity, String saveQuery, EntityToPreparedStatementMapper<E> mapper);

    Optional<E> findById(long id, String query, ResultSetToEntityMapper<E> mapper);

    boolean deleteById(long id, String query, ResultSetToEntityMapper<E> mapper);

    List<E> findAll(String query, ResultSetToEntityMapper<E> mapper);

    boolean update(E entity, String saveQuery, EntityToPreparedStatementMapper<E> mapper);
}
