package dal.dao;

import dal.entity.Bill;
import dal.exeption.AskedDataIsNotCorrect;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Declares an interface for work with {@link Bill}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface BillDao {

    List<Bill> getInfoToPayBillByUserId(long userId, Locale locale);
    /**
     * @throws AskedDataIsNotCorrect if noting found
     */
    long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotCorrect;


    List<Bill> getHistoricBillsByUserId(long userId, Integer offset, Integer limit);

    boolean murkBillAsPayed(long billId) throws SQLException;

    boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight) throws SQLException;

    long countAllBillsByUserId(long userId);
}
