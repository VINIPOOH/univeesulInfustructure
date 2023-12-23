package logiclayer.service.impl;

import dal.dao.BillDao;
import dal.dao.DeliveryDao;
import dal.dao.UserDao;
import dal.entity.Bill;
import dal.exeption.AskedDataIsNotCorrect;
import dto.BillDto;
import dto.BillInfoToPayDto;
import dto.DeliveryOrderCreateDto;
import dto.mapper.Mapper;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.anotation.Transaction;
import logiclayer.exeption.FailCreateDeliveryException;
import logiclayer.exeption.OperationFailException;
import logiclayer.exeption.UnsupportableWeightFactorException;
import logiclayer.service.BillService;
import logiclayer.service.ServicesConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Implements an interface for work with bills
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class BillServiceImpl implements BillService {

    @InjectByType
    private BillDao billDao;
    @InjectByType
    private UserDao userDao;
    @InjectByType
    private DeliveryDao deliveryDao;

    @Override
    public List<BillInfoToPayDto> getInfoToPayBillsByUserID(long userId, Locale locale) {

        return billDao.getInfoToPayBillByUserId(userId, locale).stream()
                .map(getMapperBillInfoToPayDto(locale)::map)
                .collect(Collectors.toList());
    }

    /**
     * @throws OperationFailException if not enough money or bill is already paid
     */
    @Override
    @Transaction
    public void payForDelivery(long userId, long billId) throws OperationFailException {

        try {
            if (userDao.withdrawUserBalanceOnSumIfItPossible(userId,
                    billDao.getBillCostIfItIsNotPaid(billId, userId))
                    && billDao.murkBillAsPayed(billId)) {
                return;
            }
            throw new OperationFailException();
        } catch (SQLException e) {
            throw new OperationFailException();
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            throw new OperationFailException();
        }

    }

    @Override
    public long countAllBillsByUserId(long userId) {
        return billDao.countAllBillsByUserId(userId);
    }

    /**
     * @throws UnsupportableWeightFactorException if there is no correct tariffWeightFactor fore this way
     * @throws FailCreateDeliveryException if was incorrect data inputted
     */
    @Override
    @Transaction
    public void initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId)
            throws UnsupportableWeightFactorException, FailCreateDeliveryException {

        try {
            long newDeliveryId = deliveryDao.createDelivery(deliveryOrderCreateDto.getAddresseeEmail(),
                    deliveryOrderCreateDto.getLocalitySandID(),
                    deliveryOrderCreateDto.getLocalityGetID(),
                    deliveryOrderCreateDto.getDeliveryWeight());
            if (billDao.createBill(newDeliveryId, initiatorId, deliveryOrderCreateDto.getLocalitySandID()
                    , deliveryOrderCreateDto.getLocalityGetID(), deliveryOrderCreateDto.getDeliveryWeight())) {
                return;
            }
            throw new UnsupportableWeightFactorException();
        } catch (SQLException e) {
            throw new FailCreateDeliveryException();
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            throw new FailCreateDeliveryException();
        }
    }

    @Override
    public List<BillDto> getBillHistoryByUserId(long userId, int offset, int limit) {

        return billDao.getHistoricBillsByUserId(userId, offset, limit).stream()
                .map(getBillBillDtoMapper()::map)
                .collect(Collectors.toList());
    }

    private Mapper<Bill, BillInfoToPayDto> getMapperBillInfoToPayDto(Locale locale) {
        return bill -> BillInfoToPayDto.builder()
                .weight(bill.getDelivery().getWeight())
                .price(bill.getCostInCents())
                .deliveryId(bill.getDelivery().getId())
                .billId(bill.getId())
                .addreeseeEmail(bill.getDelivery().getAddressee().getEmail())
                .localitySandName(locale.getLanguage().equals(ServicesConstants.RUSSIAN_LANG_COD) ?
                        bill.getDelivery().getWay().getLocalitySand().getNameRu() :
                        bill.getDelivery().getWay().getLocalitySand().getNameEn())
                .localityGetName(locale.getLanguage().equals(ServicesConstants.RUSSIAN_LANG_COD) ?
                        bill.getDelivery().getWay().getLocalityGet().getNameRu() :
                        bill.getDelivery().getWay().getLocalityGet().getNameEn())
                .build();
    }

    private Mapper<Bill, BillDto> getBillBillDtoMapper() {
        return bill -> BillDto.builder()
                .id(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .isDeliveryPaid(bill.getIsDeliveryPaid())
                .costInCents(bill.getCostInCents())
                .dateOfPay(bill.getDateOfPay())
                .build();
    }
}
