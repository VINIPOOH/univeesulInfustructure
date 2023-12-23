package logiclayer.service;

import dto.BillDto;
import dto.BillInfoToPayDto;
import dto.DeliveryOrderCreateDto;
import logiclayer.exeption.FailCreateDeliveryException;
import logiclayer.exeption.OperationFailException;
import logiclayer.exeption.UnsupportableWeightFactorException;

import java.util.List;
import java.util.Locale;

/**
 * Declares an interface for work with bills
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface BillService {
    List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale);
    /**
     * @throws OperationFailException if not enough money or bill is already paid
     */
    void payForDelivery(long userId, long billId) throws OperationFailException;

    long countAllBillsByUserId(long userId);
    /**
     * @throws UnsupportableWeightFactorException if there is no correct tariffWeightFactor fore this way
     * @throws FailCreateDeliveryException if was incorrect data inputted
     */
    void initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, FailCreateDeliveryException;

    List<BillDto> getBillHistoryByUserId(long userId, int offset, int limit);
}
