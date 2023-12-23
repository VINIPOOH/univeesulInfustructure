package dal.dao.impl;

import dal.dao.BillDao;
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
import java.util.List;
import java.util.Locale;

/**
 * Implements an interface for work with {@link Bill}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class JDBCBillDao extends JDBCAbstractGenericDao<Bill> implements BillDao {
    private static final String BILL_ID = "bill_id";
    private static final String PRICE = "price";
    private static final String ADDRESSEE_EMAIL = "addressee_email";
    private static final String DELIVERY_ID = "delivery_id";
    private static final String WEIGHT = "weight";
    private static final String LOCALITY_GET_NAME = "locality_get_name";
    private static final String LOCALITY_SAND_NAME = "locality_sand_name";
    private static final String IS_DELIVERY_PAID = "is_delivery_paid";
    private static final String COST_IN_CENTS = "cost_in_cents";
    private static final String DATE_OF_PAY = "date_of_pay";
    private static final String BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID =
            "bill.create.by.cost.delivery.id.user.id";
    private static final String BILL_INFO_TO_PAY_BILL_BY_USER_ID_EN =
            "bill.pay.info.select.by.sender.id.en";
    private static final String GET_BILL_PRISE_IF_NOT_PAID =
            "bill.get.prise.if.not.paid";
    private static final String SET_BILL_IS_PAID_TRUE =
            "bill.set.is.paid.true";
    private static final String BILLS_HISTORY_BY_USER_ID =
            "bill.history.by.user.id";
    private static final String BILL_INFO_TO_PAY_BILL_BY_USER_ID_RU =
            "bill.pay.info.select.by.sender.id.ru";
    private static final String COUNT_ALL_NOT_PAYED_BILLS_BY_USER_ID =
            "count.all.not.payed.bills.by.user.id";
    private static final Logger log = LogManager.getLogger(JDBCBillDao.class);


    @Override
    public List<Bill> getInfoToPayBillByUserId(long userId, Locale locale) {
        log.debug("getInfoToPayBillByUserId");


        return findAllByLongParam(userId, getDbRequestsString(BILL_INFO_TO_PAY_BILL_BY_USER_ID_EN), resultSet -> Bill.builder()
                .id(resultSet.getLong(BILL_ID))
                .costInCents(resultSet.getLong(PRICE))
                .delivery(Delivery.builder()
                        .addressee(User.builder().email(resultSet.getString(ADDRESSEE_EMAIL)).build())
                        .id(resultSet.getLong(DELIVERY_ID))
                        .weight(resultSet.getInt(WEIGHT))
                        .way(
                                Way.builder()
                                        .localityGet(Locality.builder().nameEn(resultSet.getString(LOCALITY_GET_NAME)).build())
                                        .localitySand(Locality.builder().nameEn(resultSet.getString(LOCALITY_SAND_NAME)).build())
                                        .build()
                        )
                        .build())
                .build());
    }

    /**
     * @throws AskedDataIsNotCorrect if noting found
     */
    @Override
    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotCorrect {
        log.debug("getBillCostIfItIsNotPaid");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(GET_BILL_PRISE_IF_NOT_PAID))) {
            preparedStatement.setLong(1, billId);
            preparedStatement.setLong(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
            throw new AskedDataIsNotCorrect();
        } catch (SQLException e) {
            log.error("SQLException", e);
            throw new DBRuntimeException();
        }
    }


    @Override
    public List<Bill> getHistoricBillsByUserId(long userId, Integer offset, Integer limit) {
        log.debug("getHistoricBailsByUserId");

        return findAllByLongParamPageable(userId, offset, limit, getDbRequestsString(BILLS_HISTORY_BY_USER_ID), resultSet -> (Bill.builder()
                .id(resultSet.getLong(BILL_ID))
                .delivery(Delivery.builder().id(resultSet.getLong(DELIVERY_ID)).build())
                .isDeliveryPaid(resultSet.getBoolean(IS_DELIVERY_PAID))
                .costInCents(resultSet.getLong(COST_IN_CENTS))
                .dateOfPay(resultSet.getTimestamp(DATE_OF_PAY).toLocalDateTime().toLocalDate())
                .build()));
    }

    @Override
    public long countAllBillsByUserId(long userId) {
        return countAllByLongParam(userId, getDbRequestsString(COUNT_ALL_NOT_PAYED_BILLS_BY_USER_ID));
    }

    @Override
    public boolean murkBillAsPayed(long billId) throws SQLException {
        log.debug("murkBillAsPayed");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(SET_BILL_IS_PAID_TRUE))) {
            preparedStatement.setLong(1, billId);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight)
            throws SQLException {
        log.debug("createBill");

        try (ConnectionProxy connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDbRequestsString(BILL_CREATE_BY_COST_DELIVERY_ID_USER_ID))) {
            preparedStatement.setLong(1, localitySandID);
            preparedStatement.setLong(2, localityGetID);
            preparedStatement.setInt(3, weight);
            preparedStatement.setInt(4, weight);
            preparedStatement.setLong(5, deliveryId);
            preparedStatement.setLong(6, userId);
            return preparedStatement.executeUpdate() != 0;
        }
    }

}
