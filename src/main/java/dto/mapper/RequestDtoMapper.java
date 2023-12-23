package dto.mapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Declare method for mapping an {@link HttpServletRequest} class into dto
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@FunctionalInterface
public interface RequestDtoMapper<Dto> {
    Dto mapToDto(HttpServletRequest request);
}
