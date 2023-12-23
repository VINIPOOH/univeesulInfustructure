package dto.validation;

import javax.servlet.http.HttpServletRequest;

/**
 * Validate {@link HttpServletRequest} which contains info for {@link dto.DeliveryOrderCreateDto}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class DeliveryOrderCreateDtoValidator implements Validator {
    private static final String EMAIL_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";


    @Override
    public boolean isValid(HttpServletRequest request) {
        try {
            return ((Integer.parseInt(request.getParameter("deliveryWeight")) > 0) &&
                    (Long.parseLong(request.getParameter("localityGetID")) > 0) &&
                    (Long.parseLong(request.getParameter("localitySandID")) > 0)) &&
                    isStringValid(request.getParameter("addresseeEmail"));
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isStringValid(String param) {
        return param.matches(DeliveryOrderCreateDtoValidator.EMAIL_REGEX);
    }

}
