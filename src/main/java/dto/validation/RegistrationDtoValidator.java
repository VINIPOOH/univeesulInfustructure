package dto.validation;


import javax.servlet.http.HttpServletRequest;

/**
 * Validate {@link HttpServletRequest} which contains info for {@link dto.RegistrationInfoDto}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class RegistrationDtoValidator {

    private static final String LOGIN_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";


    public boolean isValid(HttpServletRequest request) {
        return isStringValid(request.getParameter("username"))
                && request.getParameter("password").equals(request.getParameter("passwordRepeat"));
    }

    private boolean isStringValid(String param) {
        return param.matches(RegistrationDtoValidator.LOGIN_REGEX);
    }

}
