package infrastructure.http;

import com.google.gson.Gson;
import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.DispatcherServlet;
import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.util.RestUrlUtilService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import static infrastructure.constant.AttributeConstants.LOGGED_USER_NAMES;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class PureHttpDispatcherServlet extends DispatcherServlet {

    private static final Logger log = LogManager.getLogger(PureHttpDispatcherServlet.class);
    private static final String INFRASTRUCTURE_APPLICATION_URL_BASE_PATH = "infrastructure.application.url.base.path";

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        processGeneralHttpRequest(response, request);
    }

    private void processGeneralHttpRequest(HttpServletResponse servletResponse, HttpServletRequest request) throws IOException, ServletException {
        switch (request.getMethod()) {
            case "GET":
                doGet(request, servletResponse);
                break;
            case "POST":
                doPost(request, servletResponse);
                break;
            case "PUT":
                doPut(request, servletResponse);
                break;
            case "DELETE":
                doDelete(request, servletResponse);
                break;
        }
    }

    private void doGet(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("servlet called with request - " + request.getRequestURI());

        passOver(request, response, getMultipleMethodCommand(request).doGet(request));
    }

    private void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("servlet called with request - " + request.getRequestURI());
        if (request.getParameter("_method") != null) {
            switch (request.getParameter("_method")) {
                case "get":
                    doGet(request, response);
                    return;
                case "put":
                    doPut(request, response);
                    return;
                case "delete":
                    doDelete(request, response);
                    return;
                case "post":
                default:
                    passOver(request, response, getMultipleMethodCommand(request).doPost(request));
                    return;
            }
        }

        passOver(request, response, getMultipleMethodCommand(request).doPost(request));
    }

    private void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("servlet called with request - " + request.getRequestURI());

        passOver(request, response, getMultipleMethodCommand(request).doPut(request));
    }

    private void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("servlet called with request - " + request.getRequestURI());

        passOver(request, response, getMultipleMethodCommand(request).doDelete(request));
    }

    private MultipleMethodController getMultipleMethodCommand(HttpServletRequest request) {
        return ApplicationContextImpl.getContext()
                .getHttpCommand(request.getRequestURI().replaceFirst
                        (".*" + context.getPropertyValue(INFRASTRUCTURE_APPLICATION_URL_BASE_PATH), ""));
    }
}
