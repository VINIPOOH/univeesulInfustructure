package logiclayer.service;

import dto.DeliveryInfoRequestDto;
import dto.DeliveryInfoToGetDto;
import dto.PriceAndTimeOnDeliveryDto;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Declares an interface for work with deliveries
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface DeliveryService {
    Optional<PriceAndTimeOnDeliveryDto> getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto);

    List<DeliveryInfoToGetDto> getInfoToGetDeliveriesByUserID(long userId, Locale locale);

    boolean confirmGettingDelivery(long userId, long deliveryId);
}
