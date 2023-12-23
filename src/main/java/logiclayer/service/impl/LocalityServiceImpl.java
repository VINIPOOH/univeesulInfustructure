package logiclayer.service.impl;


import dal.dao.LocalityDao;
import dal.entity.Locality;
import dto.LocaliseLocalityDto;
import dto.mapper.Mapper;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.LocalityService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static logiclayer.service.ServicesConstants.RUSSIAN_LANG_COD;

/**
 * Implements an interface for work with localities
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
public class LocalityServiceImpl implements LocalityService {
    @InjectByType
    private LocalityDao localityDao;

    @Override
    public List<LocaliseLocalityDto> getLocaliseLocalities(Locale locale) {

        return localityDao.findAllLocaliseLocalitiesWithoutConnection(locale).stream()
                .map(getLocalityToLocaliseLocalityDto(locale)::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocaliseLocalityDto> getLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id) {
        return localityDao.findLocaliseLocalitiesGetByLocalitySendId(locale, id).stream()
                .map(getLocalityToLocaliseLocalityDto(locale)::map)
                .collect(Collectors.toList());
    }

    private Mapper<Locality, LocaliseLocalityDto> getLocalityToLocaliseLocalityDto(Locale locale) {
        return locality -> LocaliseLocalityDto.builder()
                .id(locality.getId())
                .name(locale.getLanguage().equals(RUSSIAN_LANG_COD) ?
                        locality.getNameRu() :
                        locality.getNameEn())
                .build();
    }
}
