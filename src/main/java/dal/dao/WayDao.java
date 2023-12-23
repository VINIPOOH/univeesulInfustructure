package dal.dao;

import dal.dto.DeliveryCostAndTimeDto;

import java.util.Optional;

/**
 * Declares an interface for work with {@link dal.entity.Way}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface WayDao {
    Optional<DeliveryCostAndTimeDto> findByLocalitySandIdAndLocalityGetId(long localitySandID, long localityGetID, int weight);


}