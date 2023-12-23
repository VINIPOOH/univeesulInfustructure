package logiclayer.service;

import dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

/**
 * Declares an interface for work with localities
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface LocalityService {
    List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale);

    List<LocaliseLocalityDto> getLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id);
}
