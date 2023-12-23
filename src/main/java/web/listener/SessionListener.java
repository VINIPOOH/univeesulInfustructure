package web.listener;

import dal.entity.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

import static web.constant.AttributeConstants.LOGGED_USER_NAMES;
import static web.constant.AttributeConstants.SESSION_USER;

/**
 * Control users logout: close their sessions and delete them from logged user map
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        Map<String, HttpSession> loggedUsers = (Map<String, HttpSession>) httpSessionEvent
                .getSession().getServletContext()
                .getAttribute(LOGGED_USER_NAMES);
        User user = (User) httpSessionEvent.getSession().getAttribute(SESSION_USER);
        if (user != null) {
            loggedUsers.remove(user.getEmail());
        }

    }
}
