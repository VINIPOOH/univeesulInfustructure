package web.comand.impl;

import dto.RegistrationInfoDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.RegistrationDtoValidator;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.exeption.OccupiedLoginException;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

/**
 * Process "anonymous/registration" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@NeedConfig
@HttpEndpoint("/http/anonymous/registration")
public class RegistrationController implements MultipleMethodController {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_REPEAT = "passwordRepeat";
    private static final String INPUT_HAS_ERRORS = "inputHasErrors";
    private static final String INPUT_LOGIN_ALREADY_TAKEN = "inputLoginAlreadyTaken";
    private static final Logger log = LogManager.getLogger(RegistrationController.class);
    @InjectByType
    private RegistrationDtoValidator registrationInfoDtoValidator;
    @InjectByType
    private UserService userService;

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        boolean isValid = registrationInfoDtoValidator.isValid(request);
        log.debug("isValidRequest = " + isValid);

        if (!isValid) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
        }
        return processingServiceRegistrationRequest(request, getRegistrationInfoDtoRequestDtoMapper(request).mapToDto(request));
    }

    private RequestDtoMapper<RegistrationInfoDto> getRegistrationInfoDtoRequestDtoMapper(HttpServletRequest request) {
        return req -> RegistrationInfoDto.builder()
                .username(request.getParameter(USERNAME))
                .password(request.getParameter(PASSWORD))
                .passwordRepeat(request.getParameter(PASSWORD_REPEAT))
                .build();
    }

    private String processingServiceRegistrationRequest(HttpServletRequest request, RegistrationInfoDto registrationInfoDto) {
        try {
            userService.addNewUserToDB(registrationInfoDto);
            return REDIRECT_COMMAND + ANONYMOUS_FOLDER + LOGIN_REQUEST_COMMAND;
        } catch (OccupiedLoginException e) {
            request.setAttribute(INPUT_LOGIN_ALREADY_TAKEN, true);
            return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + REGISTRATION_FILE_NAME;
        }
    }
}
