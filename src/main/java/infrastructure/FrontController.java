package infrastructure;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.GenericServlet;
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
public abstract class FrontController extends GenericServlet {

    protected ApplicationContext context;
    protected static final String JSON_RESPONSE_KEY = "infrastructure.web.json.response.prefix"; //todo move to the properties
    private static final String INFRASTRUCTURE_APPLICATION_URL_REDIRECT_PREFIX = "infrastructure.web.url.redirect.prefix";

    @Override
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load MySQL JDBC Driver");
            e.printStackTrace();
        }
        final ServletContext servletContext = getServletContext();
        servletContext.setAttribute(LOGGED_USER_NAMES, new ConcurrentHashMap<String, HttpSession>());
        context = ApplicationContextImpl.getContext();
    }


    protected String getMethodCode(HttpServletRequest request) {
        if (request.getContentType() != null && request.getContentType().equals("application/x-www-form-urlencoded") && request.getParameter("_method") != null) {
            switch (request.getParameter("_method")) {
                case "GET":
                    return "GET";
                case "PUT":
                    return "PUT";
                case "DELETE":
                    return "DELETE";
                case "POST":
                default:
                    return "POST";
            }
        }
        return request.getMethod();
    }

    protected void passOver(HttpServletRequest request, HttpServletResponse response, String page) throws IOException, ServletException {
        if (page.contains(context.getPropertyValue(INFRASTRUCTURE_APPLICATION_URL_REDIRECT_PREFIX))) {
            response.sendRedirect(page.replace(context.getPropertyValue(INFRASTRUCTURE_APPLICATION_URL_REDIRECT_PREFIX), ""));
        } else if (page.startsWith(context.getPropertyValue(JSON_RESPONSE_KEY))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(page.replaceFirst(context.getPropertyValue(JSON_RESPONSE_KEY), ""));
            out.flush();
        } else {
            request.getRequestDispatcher(page).forward(request, response); //стандартная работа по пробросу на джспеху
        }
    }
}
