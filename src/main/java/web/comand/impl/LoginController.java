package web.comand.impl;

import dal.entity.User;
import dto.LoginInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.LoginDtoValidator;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static web.constant.AttributeConstants.LOGGED_USER_NAMES;
import static web.constant.AttributeConstants.SESSION_USER;
import static web.constant.PageConstance.*;

/**
 * Process "anonymous/login" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
@HttpEndpoint("/http/anonymous/login")
public class LoginController implements MultipleMethodController {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final Logger log = LogManager.getLogger(LoginController.class);
    @InjectByType
    private LoginDtoValidator loginDtoValidator;
    @InjectByType
    private UserService userService;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");
        return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        boolean isValid = loginDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);
        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
        }
        LoginInfoDto loginInfoDto = getLoginInfoDtoRequestDtoMapper(request).mapToDto(request);
        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) request
                .getSession().getServletContext()
                .getAttribute(LOGGED_USER_NAMES);
        if (loggedUsers.containsKey(loginInfoDto.getUsername())) {
            loggedUsers.get(loginInfoDto.getUsername()).invalidate();
        }
        try {
            User user = userService.loginUser(loginInfoDto);
            loggedUsers.put(loginInfoDto.getUsername(), request.getSession());
            request.getSession().setAttribute(SESSION_USER, user);
            return REDIRECT_COMMAND + USER_PROFILE_REQUEST_COMMAND;
        } catch (NoSuchUserException ignored) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + LOGIN_FILE_NAME;
        }
    }

    private RequestDtoMapper<LoginInfoDto> getLoginInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> LoginInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .build();
    }

}
