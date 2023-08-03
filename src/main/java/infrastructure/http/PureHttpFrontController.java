package infrastructure.http;

import infrastructure.ApplicationContextImpl;
import infrastructure.FrontController;
import infrastructure.http.controller.MultipleMethodController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class PureHttpFrontController extends FrontController {

    private static final Logger log = LogManager.getLogger(PureHttpFrontController.class);
    private static final String INFRASTRUCTURE_APPLICATION_URL_BASE_PATH = "infrastructure.application.url.base.path";

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        processGeneralHttpRequest(response, request);
    }

    private void processGeneralHttpRequest(HttpServletResponse servletResponse, HttpServletRequest request) throws IOException, ServletException {
        switch (getMethodCode(request)) {
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
        return context
                .getHttpCommand(request.getRequestURI().replaceFirst
                        (".*" + context.getPropertyValue(INFRASTRUCTURE_APPLICATION_URL_BASE_PATH), ""));
    }
}
