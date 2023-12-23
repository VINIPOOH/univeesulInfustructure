package web.comand.impl;

import dal.entity.User;
import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.Singleton;
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
 * Process "user/logout" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@HttpEndpoint("/http/user/logout")
public class LogOutController implements MultipleMethodController {
    private static final Logger log = LogManager.getLogger(LogOutController.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) request
                .getSession().getServletContext()
                .getAttribute(LOGGED_USER_NAMES);
        loggedUsers.remove(((User) request.getSession().getAttribute(SESSION_USER)).getEmail());
        request.getSession().invalidate();
        return REDIRECT_COMMAND + LOGIN_REQUEST_COMMAND;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
