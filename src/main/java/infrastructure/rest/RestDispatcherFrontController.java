package infrastructure.rest;

import com.google.gson.Gson;
import infrastructure.ApplicationContextImpl;
import infrastructure.FrontController;
import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.util.RestUrlUtilService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class RestDispatcherFrontController extends FrontController {

    private static final Logger log = LogManager.getLogger(RestDispatcherFrontController.class);
    private static final RestUrlUtilService REST_URL_UTIL_SERVICE = new RestUrlUtilService();
    private static final String INFRASTRUCTURE_APPLICATION_REST_URL_PREFIX = "infrastructure.application.rest.url.prefix";
    private static final String INFRASTRUCTURE_APPLICATION_URL_BASE_PATH = "infrastructure.application.url.base.path";

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String logicUrlPath = request.getRequestURI().replaceFirst(
                ".*" + context.getPropertyValue(INFRASTRUCTURE_APPLICATION_URL_BASE_PATH), "");
        passOver(request, response, processRestRequest(logicUrlPath, request, response));
    }

    private String processRestRequest(String logicUrlPath, HttpServletRequest request,
                                      HttpServletResponse response) {
        logicUrlPath = logicUrlPath.replaceFirst(context.getPropertyValue(INFRASTRUCTURE_APPLICATION_REST_URL_PREFIX), "");
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
}
