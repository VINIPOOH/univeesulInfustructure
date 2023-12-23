package dto.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Validate {@link HttpServletRequest} which contains id
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class IDValidatorImpl implements IDValidator {
    private static final Logger log = LogManager.getLogger(IDValidatorImpl.class);

    public boolean isValid(HttpServletRequest request, String[] idFieldNames) {
        try {
            for (String str : idFieldNames) {
                if (Long.parseLong(request.getParameter(str)) <= 0) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException ex) {
            log.error("string where expecting long");
            return false;
        }
    }
}
