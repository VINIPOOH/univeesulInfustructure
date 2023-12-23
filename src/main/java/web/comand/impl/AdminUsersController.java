package web.comand.impl;

import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import infrastructure.http.controller.MultipleMethodController;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

/**
 * Process "admin/users" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton(isLazy = true)
@NeedConfig
@HttpEndpoint("/http/admin/users")
public class AdminUsersController implements MultipleMethodController {
    private static final String USERS_LIST = "usersList";
    private static final Logger log = LogManager.getLogger(AdminUsersController.class);

    @InjectByType
    private UserService userService;


    @Override
    public String doGet(HttpServletRequest request) {
        log.debug(request.getMethod() + " admin");

        request.setAttribute(USERS_LIST, userService.getAllUsers());
        return MAIN_WEB_FOLDER + ADMIN_FOLDER + USERS_JSP;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
