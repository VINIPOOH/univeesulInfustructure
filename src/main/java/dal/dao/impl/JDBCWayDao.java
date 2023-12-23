package dal.dao.impl;

import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Way;
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
import java.util.Optional;

/**
 * Implements an interface for work with {@link Way}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class JDBCWayDao extends JDBCAbstractGenericDao<Way> implements WayDao {
    private static final String PRICE = "price";
    private static final String TIME_ON_WAY_IN_DAYS = "time_on_way_in_days";
    private static final String GET_COST_AND_TIME_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT =
            "way.find.price.and.time.by.locality_send_id.and.locality_get_id.and.weight";
    private static final Logger log = LogManager.getLogger(JDBCWayDao.class);


    @Override
    public Optional<DeliveryCostAndTimeDto> findByLocalitySandIdAndLocalityGetId(long localitySandID, long localityGetID, int weight) {
        log.debug("findByLocalitySandIdAndLocalityGetId");

        ResultSetToEntityMapper<DeliveryCostAndTimeDto> mapper = getDeliveryCostAndTimeDtoResultSetToEntityMapper();

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(GET_COST_AND_TIME_ON_DELIVERY_BY_LOCALITY_SEND_ID_LOCALITY_GET_ID_DELIVERY_WEIGHT))) {

            preparedStatement.setLong(1, localitySandID);
            preparedStatement.setLong(2, localityGetID);
            preparedStatement.setInt(3, weight);
            preparedStatement.setInt(4, weight);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapper.map(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }

    }

    private ResultSetToEntityMapper<DeliveryCostAndTimeDto> getDeliveryCostAndTimeDtoResultSetToEntityMapper() {
        return resultSet -> DeliveryCostAndTimeDto.builder()
                .costInCents(resultSet.getLong(PRICE))
                .timeOnWayInHours(resultSet.getInt(TIME_ON_WAY_IN_DAYS))
                .build();
    }

}
