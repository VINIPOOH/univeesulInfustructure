package infrastructure.controller;

import infrastructure.anotation.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static infrastructure.constant.PageConstance.ERROR_404_COMMAND;
import static infrastructure.constant.PageConstance.REDIRECT_COMMAND;


/**
 * Process request for which application context has no mapping
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Singleton
public class PhantomController implements MultipleMethodController {
    private static final Logger log = LogManager.getLogger(PhantomController.class);

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");

        return REDIRECT_COMMAND + ERROR_404_COMMAND;
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
