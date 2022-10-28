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

    ApplicationContext context;

    @Override
    public void init() {
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
        String logicUrlPath = request.getRequestURI().replaceFirst(".*/delivery/", "");
        if (logicUrlPath.startsWith("rest/")) {//todo етот иф можно заменить патерном команда в будйщем если потребуется
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
        logicUrlPath = logicUrlPath.replaceFirst("rest/", "");
        RestUrlCommandProcessorInfo restCommandProcessorInfo = ApplicationContextImpl.getContext().getRestCommand(logicUrlPath, request.getMethod());

        Object[] parametersToPassInInvocation = RestUrlUtilService.retrieveParametersFromRestUrl(logicUrlPath, restCommandProcessorInfo, request, response);
        //todo тут если потребуется подходящее место для добавления автопередачи реквеста и респонса дальше в методы

        try {
            Object invoke = restCommandProcessorInfo.getProcessorsMethod().invoke(restCommandProcessorInfo.getCommandProcessor(), parametersToPassInInvocation);
            return JSON_RESPONSE + new Gson().toJson(invoke);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return "null";
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
        return ApplicationContextImpl.getContext()
                .getHttpCommand(request.getRequestURI().replaceFirst(".*/delivery/", ""));
    }

    private void passOver(HttpServletRequest request, HttpServletResponse response, String page) throws IOException, ServletException {
        if (page.contains("redirect:")) {
            response.sendRedirect(page.replace("redirect:", "/delivery/")); //
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
