package dto.validation;

import javax.servlet.http.HttpServletRequest;

/**
 * Declare method for validate {@link HttpServletRequest}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@FunctionalInterface
public interface Validator {
    boolean isValid(HttpServletRequest request);

}
