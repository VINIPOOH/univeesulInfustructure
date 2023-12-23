package dal.dao.impl;

import dal.dao.DeliveryDao;
import dal.entity.*;
import dal.exeption.AskedDataIsNotCorrect;
import dal.exeption.DBRuntimeException;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.dal.conection.ConnectionProxy;
import infrastructure.dal.conection.dao.JDBCAbstractGenericDao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

/**
 * Implements an interface for work with {@link Delivery}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class JDBCDeliveryDao extends JDBCAbstractGenericDao<Delivery> implements DeliveryDao {
    private static final String DELIVERY_INFO_TO_GET_BY_USER_ID_EN =
            "delivery.get.not.received.deliveries.by.user.id.en";
    private static final String DELIVERY_INFO_TO_GET_BY_USER_ID_RU =
            "delivery.get.not.received.deliveries.by.user.id.ru";
    private static final String SET_DELIVERY_RECIWED_STATUSE_TRUE =
            "delivery.set.received.status.true";
    private static final String CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID =
            "create.delivery.by.weight.id.locality.send.idlocality.get.adresee.email.adresser.id";
    private static final String LOCALITY_SEND_COLUMN_NAME = "locality_sand_name";
    private static final String LOCALITY_GET_COLUMN_NAME = "locality_get_name";
    private static final Logger log = LogManager.getLogger(JDBCDeliveryDao.class);


    @Override
    public List<Delivery> getDeliveryInfoToGet(long userId, Locale locale) {
        log.debug("getDeliveryInfoToGet");

        return findAllByLongParam(userId, getDbRequestsString(DELIVERY_INFO_TO_GET_BY_USER_ID_EN), resultSet -> Delivery.builder()
                .id(resultSet.getLong("id"))
                .bill(Bill.builder().user(User.builder().email(resultSet.getString("email")).build()).build())
                .way(
                        Way.builder()
                                .localityGet(Locality.builder().nameEn(resultSet.getString(LOCALITY_GET_COLUMN_NAME)).build())
                                .localitySand(Locality.builder().nameEn(resultSet.getString(LOCALITY_SEND_COLUMN_NAME)).build())
                                .build()
                )
                .build());
    }

    @Override
    public boolean confirmGettingDelivery(long userId, long deliveryId) {
        log.debug("confirmGettingDelivery");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(SET_DELIVERY_RECIWED_STATUSE_TRUE))) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, deliveryId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }

    }

    /**
     * @throws AskedDataIsNotCorrect if noting found
     */
    @Override
    public long createDelivery(String addreeseeEmail, long localitySandID, long localityGetID, int weight)
            throws AskedDataIsNotCorrect {
        log.debug("createDelivery");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     getDbRequestsString(CREATE_DELIVERY_BY_WEIGHT_ID_LOCALITY_SEND_IDLOCALITY_GET_ADRESEE_EMAIL_ADRESSER_ID), Statement.RETURN_GENERATED_KEYS)) {

            prepareAndExecuteStatment(addreeseeEmail, localitySandID, localityGetID, weight, preparedStatement);
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            throw new AskedDataIsNotCorrect();
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new AskedDataIsNotCorrect();
        }
    }

    private void prepareAndExecuteStatment(String addreeseeEmail, long localitySandID, long localityGetID, int weight, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, addreeseeEmail);
        preparedStatement.setLong(2, localitySandID);
        preparedStatement.setLong(3, localityGetID);
        preparedStatement.setInt(4, weight);
        preparedStatement.executeUpdate();
    }
}
