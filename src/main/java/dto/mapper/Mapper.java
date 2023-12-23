package dto.mapper;


/**
 * Declare method for mapping an entity class into dto
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@FunctionalInterface
public interface Mapper<E, Dto> {
    Dto map(E entity);
}
