package infrastructure.http;

import infrastructure.ApplicationContext;
import infrastructure.ApplicationContextImpl;
import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.http.controller.MultipleMethodController;
import infrastructure.util.RestUrlUtilService;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static infrastructure.constant.AttributeConstants.LOGGED_USER_NAMES;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class DispatcherServlet extends GenericServlet {

    public static final String JSON_RESPONSE = "json-response:";
    private static final Logger log = LogManager.getLogger(DispatcherServlet.class);
    private static final RestUrlUtilService REST_URL_UTIL_SERVICE = new RestUrlUtilService();

    ApplicationContext context;

    @Override
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load MySQL JDBC Driver");
            e.printStackTrace();
        }
        log.debug("initialization started");
        final ServletContext servletContext = getServletContext();
        servletContext.setAttribute(LOGGED_USER_NAMES, new ConcurrentHashMap<String, HttpSession>());
        context = ApplicationContextImpl.getContext();
        log.debug("start tcp server");
        log.debug("initialization done");
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String logicUrlPath = request.getRequestURI().replaceFirst(".*/delivery/", "");//todo property
        if (logicUrlPath.startsWith("/rest/")) {//todo property
            passOver(request, response, processRestRequest(logicUrlPath, request, response));
        } else {
            processGeneralHttpRequest((HttpServletResponse) servletResponse, request);
        }
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

    private String processRestRequest(String logicUrlPath, HttpServletRequest request,
                                      HttpServletResponse response) {
        logicUrlPath = logicUrlPath.replaceFirst("/rest", "");
        RestUrlCommandProcessorInfo restCommandProcessorInfo = ApplicationContextImpl.getContext().getRestCommand(logicUrlPath, request.getMethod());

        Object[] parametersToPassInInvocation = populateMethodParametersValues(logicUrlPath, request, response, restCommandProcessorInfo);

        try {
            Object invoke = restCommandProcessorInfo.getProcessorsMethod().invoke(restCommandProcessorInfo.getCommandProcessor(), parametersToPassInInvocation);
            return JSON_RESPONSE + new Gson().toJson(invoke);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "null";
        }
    }

    private static Object[] populateMethodParametersValues(String logicUrlPath, HttpServletRequest request, HttpServletResponse response, RestUrlCommandProcessorInfo restCommandProcessorInfo) {
        Object[] parametersToPassInInvocation = REST_URL_UTIL_SERVICE.createParametersArray(restCommandProcessorInfo);
        REST_URL_UTIL_SERVICE.populateParametersFromRequestUrl(logicUrlPath, restCommandProcessorInfo, parametersToPassInInvocation);
        REST_URL_UTIL_SERVICE.populateHttpRequest(request, restCommandProcessorInfo, parametersToPassInInvocation);
        REST_URL_UTIL_SERVICE.populateHttpResponse(response, restCommandProcessorInfo, parametersToPassInInvocation);
        REST_URL_UTIL_SERVICE.populateRequestBody(request, restCommandProcessorInfo, parametersToPassInInvocation);
        return parametersToPassInInvocation;
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
                .getHttpCommand(request.getRequestURI().replaceFirst(".*/delivery/", ""));//todo generify this delivery in some property
    }

    private void passOver(HttpServletRequest request, HttpServletResponse response, String page) throws IOException, ServletException {
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "")); //todo property
        } else if (page.startsWith(JSON_RESPONSE)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(page.replaceFirst(JSON_RESPONSE, ""));
            out.flush();
        } else {
            request.getRequestDispatcher(page).forward(request, response); //стандартная работа по пробросу на джспеху
        }
    }
}
