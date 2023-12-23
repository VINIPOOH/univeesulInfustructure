package dto.validation;


import infrastructure.anotation.Singleton;

import javax.servlet.http.HttpServletRequest;

/**
 * Validate {@link HttpServletRequest} which contains info for {@link dto.LoginInfoDto}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
public class LoginDtoValidator {

    private static final String LOGIN_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";

    public boolean isValid(HttpServletRequest request) {
        return isStringValid(request.getParameter("username"));
    }

    private boolean isStringValid(String param) {
        return param.matches(LoginDtoValidator.LOGIN_REGEX);
    }

}
