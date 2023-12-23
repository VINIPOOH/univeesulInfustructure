package dto.validation;

import javax.servlet.http.HttpServletRequest;

/**
 * Declare method for validate {@link HttpServletRequest} which contains id
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface IDValidator {
    boolean isValid(HttpServletRequest request, String... idFieldNames);
}
