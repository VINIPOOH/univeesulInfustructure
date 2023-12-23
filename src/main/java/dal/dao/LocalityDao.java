package dal.dao;

import dal.entity.Locality;

import java.util.List;
import java.util.Locale;

/**
 * Declares an interface for work with {@link Locality}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface LocalityDao {


    List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale);

    List<Locality> findLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id);

}
