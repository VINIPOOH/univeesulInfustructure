package web.comand.impl;

import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.*;

/**
 * Process "anonymous/index" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@HttpEndpoint({"/http/anonymous/index", "/http/"})
public class IndexController implements MultipleMethodController {
    private static final Logger log = LogManager.getLogger(IndexController.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + ANONYMOUS_FOLDER + INDEX_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
