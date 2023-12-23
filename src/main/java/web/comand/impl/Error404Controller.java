package web.comand.impl;

import infrastructure.anotation.HttpEndpoint;
import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import infrastructure.http.controller.MultipleMethodController;

import javax.servlet.http.HttpServletRequest;

import static web.constant.PageConstance.ERROR_404_FILE_NAME;
import static web.constant.PageConstance.MAIN_WEB_FOLDER;

/**
 * Process "404" page requests
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
@HttpEndpoint("/http/404")
public class Error404Controller implements MultipleMethodController {
    private static final Logger log = LogManager.getLogger(Error404Controller.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return MAIN_WEB_FOLDER + ERROR_404_FILE_NAME;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
