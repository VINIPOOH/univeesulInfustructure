package dal.dao;

import dal.entity.Delivery;
import dal.exeption.AskedDataIsNotCorrect;

import java.util.List;
import java.util.Locale;

/**
 * Declares an interface for work with {@link Delivery}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface DeliveryDao {

    List<Delivery> getDeliveryInfoToGet(long userId, Locale locale);

    boolean confirmGettingDelivery(long userId, long deliveryId);
    /**
     * @throws AskedDataIsNotCorrect if noting found
     */
    long createDelivery(String addreeseeEmail, long localitySandID, long localityGetID, int weight) throws AskedDataIsNotCorrect;

}
