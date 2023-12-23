package dal.dao.impl;

import dal.dao.LocalityDao;
import dal.entity.Locality;
import dal.exeption.DBRuntimeException;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.dal.conection.ConnectionProxy;
import infrastructure.dal.conection.dao.JDBCAbstractGenericDao;
import infrastructure.dal.conection.dao.ResultSetToEntityMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Implements an interface for work with {@link Locality}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class JDBCLocalityDao extends JDBCAbstractGenericDao<Locality> implements LocalityDao {
    private static final String LOCALITY_FIND_ALL_RU = "locality.find.all.ru";
    private static final String LOCALITY_FIND_ALL_EN = "locality.find.all.en";
    private static final String LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_RU = "locality.get.find.by.locality.send.id.ru";
    private static final String LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_EN = "locality.get.find.by.locality.send.id.en";
    private static final String ID = "id";
    private static final String LOCALITY_NAME = "name";
    private static final Logger log = LogManager.getLogger(JDBCLocalityDao.class);


    @Override
    public List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale) {
        log.debug("findAllLocaliseLocalitiesWithoutConnection");

        ResultSetToEntityMapper<Locality> mapper = getLocaliseLocalityMapper(locale);
        String localedQuery;
        localedQuery = getDbRequestsString(LOCALITY_FIND_ALL_EN);
        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
            List<Locality> result;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    @Override
    public List<Locality> findLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id) {
        ResultSetToEntityMapper<Locality> mapper = getLocaliseLocalityMapper(locale);
        String localedQuery;
        localedQuery = getDbRequestsString(LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_EN);

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
            preparedStatement.setLong(1, id);
            List<Locality> result;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }

    private ResultSetToEntityMapper<Locality> getLocaliseLocalityMapper(Locale locale) {
        return resultSet -> Locality.builder()
                .id(resultSet.getLong(ID))
                .nameEn(resultSet.getString(LOCALITY_NAME))
                .build();
    }

}
