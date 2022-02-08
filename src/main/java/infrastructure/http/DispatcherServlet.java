package infrastructure.http;

import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.http.controller.MultipleMethodController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import static infrastructure.constant.AttributeConstants.LOGGED_USER_NAMES;


/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class DispatcherServlet extends javax.servlet.http.HttpServlet {

    public static final String JSON_RESPONSE = "json-response:";
    private static final Logger log = LogManager.getLogger(DispatcherServlet.class);

    @Override
    public void init() {
        log.debug("initialization started");
        final ServletContext servletContext = getServletContext();
        servletContext.setAttribute(LOGGED_USER_NAMES, new ConcurrentHashMap<String, HttpSession>());
        final ApplicationContext context = ApplicationContextImpl.getContext();
        log.debug("start tcp server");
        log.debug("initialization done");
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("servlet called with request - " + request.getRequestURI());

        passOver(request, response, getMultipleMethodCommand(request).doGet(request));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("servlet called with request - " + request.getRequestURI());

        passOver(request, response, getMultipleMethodCommand(request).doPost(request));
    }

    private MultipleMethodController getMultipleMethodCommand(HttpServletRequest request) {
        return ApplicationContextImpl.getContext()
                .getHttpCommand(request.getRequestURI().replaceFirst(".*/delivery/", ""));
    }

    private void passOver(HttpServletRequest request, HttpServletResponse response, String page) throws IOException, ServletException {
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "/delivery/"));
        } else if (page.startsWith(JSON_RESPONSE)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(page.replaceFirst(JSON_RESPONSE, ""));
            out.flush();
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}