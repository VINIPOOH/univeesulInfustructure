package logiclayer.service.impl;


import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import dto.DeliveryInfoRequestDto;
import dto.DeliveryInfoToGetDto;
import dto.PriceAndTimeOnDeliveryDto;
import dto.mapper.Mapper;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.DeliveryService;
import logiclayer.service.ServicesConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implements an interface for work with deliveries
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class DeliveryServiceImpl implements DeliveryService {
    @InjectByType
    private WayDao wayDao;
    @InjectByType
    private DeliveryDao deliveryDao;

    @Override
    public Optional<PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) {
        return wayDao.findByLocalitySandIdAndLocalityGetId(deliveryInfoRequestDto.getLocalitySandID(),
                deliveryInfoRequestDto.getLocalityGetID(), deliveryInfoRequestDto.getDeliveryWeight())
                .map(deliveryCostAndTimeDto -> getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper()
                        .map(deliveryCostAndTimeDto));
    }

    @Override
    public List<DeliveryInfoToGetDto> getInfoToGetDeliveriesByUserID(long userId, Locale locale) {

        return deliveryDao.getDeliveryInfoToGet(userId, locale).stream()
                .map(getDeliveryInfoToGetDtoMapper(locale)::map)
                .collect(Collectors.toList());
    }

    @Override
    public boolean confirmGettingDelivery(long userId, long deliveryId) {

        return deliveryDao.confirmGettingDelivery(userId, deliveryId);
    }

    private Mapper<DeliveryCostAndTimeDto, PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDtoPriceAndTimeOnDeliveryDtoMapper() {
        return deliveryCostAndTime -> PriceAndTimeOnDeliveryDto.builder()
                .costInCents(deliveryCostAndTime.getCostInCents())
                .timeOnWayInHours(deliveryCostAndTime.getTimeOnWayInHours())
                .build();
    }

    private Mapper<Delivery, DeliveryInfoToGetDto> getDeliveryInfoToGetDtoMapper(Locale locale) {
        return delivery -> DeliveryInfoToGetDto.builder()
                .addresserEmail(delivery.getBill().getUser().getEmail())
                .deliveryId(delivery.getId())
                .localitySandName(locale.getLanguage().equals(ServicesConstants.RUSSIAN_LANG_COD) ?
                        delivery.getWay().getLocalitySand().getNameRu() :
                        delivery.getWay().getLocalitySand().getNameEn())
                .localityGetName(locale.getLanguage().equals(ServicesConstants.RUSSIAN_LANG_COD) ?
                        delivery.getWay().getLocalityGet().getNameRu() :
                        delivery.getWay().getLocalityGet().getNameEn())
                .build();
    }

}
