package infrastructure.util;

import com.google.gson.Gson;
import infrastructure.RestProcessorMethodsInfo;
import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.RestUrlVariableInfo;
import infrastructure.anotation.rest.*;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RestUrlUtilService {

    public Object[] createParametersArray(RestUrlCommandProcessorInfo restCommandProcessorInfo) {
        return new Object[restCommandProcessorInfo.getProcessorsMethod().getParameters().length];
    }

    public void populateParametersFromRequestUrl(String logicUrlPath, RestUrlCommandProcessorInfo restCommandProcessorInfo, Object[] targetParametersList) {
        String[] urlSteps = logicUrlPath.split("\\/");
        urlSteps = Arrays.copyOfRange(urlSteps, 1, urlSteps.length);
        Parameter[] parametersReflection = restCommandProcessorInfo.getProcessorsMethod().getParameters();
        for (int i = 0; i < parametersReflection.length; i++) {
            Parameter parameter = parametersReflection[i];
            if (isPrimitiveType(parameter)) {
                List<RestUrlVariableInfo> restUrlVariableInfos = restCommandProcessorInfo.getRestUrlVariableInfos();
                for (RestUrlVariableInfo restUrlVariableInfo : restUrlVariableInfos) {
                    if (restUrlVariableInfo.getKey().equals(parameter.getAnnotation(RequestParam.class).paramName())) {
                        String valueOfVariable = urlSteps[restUrlVariableInfo.getNumberStepInUrl()];
                        Class<?> parameterType = parametersReflection[i].getType();
                        Object valueToSet = convertStringValueToPrimitiveType(valueOfVariable, parameterType);
                        targetParametersList[i] = valueToSet;
                    }
                }
            }
        }
    }

    public void populateHttpRequest(HttpServletRequest request, RestUrlCommandProcessorInfo restCommandProcessorInfo, Object[] targetParametersList) {
        Parameter[] parametersReflection = restCommandProcessorInfo.getProcessorsMethod().getParameters();
        for (int i = 0; i < parametersReflection.length; i++) {
            Parameter parameter = parametersReflection[i];
            if (parameter.getType().equals(HttpServletRequest.class)) {
                targetParametersList[i] = request;
            }
        }
    }


    public void populateHttpResponse(HttpServletResponse response, RestUrlCommandProcessorInfo restCommandProcessorInfo, Object[] targetParametersList) {
        Parameter[] parametersReflection = restCommandProcessorInfo.getProcessorsMethod().getParameters();
        for (int i = 0; i < parametersReflection.length; i++) {
            Parameter parameter = parametersReflection[i];
            if (parameter.getType().equals(HttpServletResponse.class)) {
                targetParametersList[i] = response;
            }
        }
    }

    @SneakyThrows
    public void populateRequestBody(HttpServletRequest request, RestUrlCommandProcessorInfo restCommandProcessorInfo, Object[] targetParametersList) {
        Parameter[] parametersReflection = restCommandProcessorInfo.getProcessorsMethod().getParameters();
        for (int i = 0; i < parametersReflection.length; i++) {
            Parameter parameter = parametersReflection[i];
            if (Arrays.stream(parameter.getAnnotations()).map(Annotation::annotationType).anyMatch(p -> p.equals(RequestBody.class))) {
                targetParametersList[i] = new Gson()
                        .fromJson(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), parameter.getType());
            }
        }
    }


    private static boolean isPrimitiveType(Parameter parameter) {
        return parameter.getType().equals(String.class) ||
                parameter.getType().equals(Long.class) ||
                parameter.getType().equals(Integer.class) ||
                parameter.getType().equals(Double.class) ||
                parameter.getType().equals(Float.class) ||
                parameter.getType().equals(int.class) ||
                parameter.getType().equals(double.class) ||
                parameter.getType().equals(long.class) ||
                parameter.getType().equals(float.class);
    }

    private static Object convertStringValueToPrimitiveType(String valueOfVariable, Class<?> parameterType) {
        if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
            return Integer.parseInt(valueOfVariable);
        } else if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
            return Long.parseLong(valueOfVariable);
        } else if (parameterType.equals(float.class) || parameterType.equals(Float.class)) {
            return Float.parseFloat(valueOfVariable);
        } else if (parameterType.equals(double.class) || parameterType.equals(Double.class)) {
            return Double.parseDouble(valueOfVariable);
        } else {
            return valueOfVariable;
        }
    }

    public static Method retrieveMethodForProcessRequest(String requestUrl, String requestMethod, RestProcessorMethodsInfo restProcessorMethodsInfo) {
        requestUrl = removeFromStringEndinSleshSimbol(requestUrl);
        switch (requestMethod) {
            case "GET":
                if (requestUrl.matches(restProcessorMethodsInfo.getPureEndingOfResource())) {
                    return restProcessorMethodsInfo.getGetAllMethod();
                } else {
                    return restProcessorMethodsInfo.getGetByIdMethod();
                }
            case "POST":
                return restProcessorMethodsInfo.getUpdateMethod();
            case "PUT":
                return restProcessorMethodsInfo.getPutMethod();
            case "DELETE":
                return restProcessorMethodsInfo.getDeleteMethod();
            default:
                throw new UnsupportedOperationException("there is no method for request " + requestUrl + "with method - " + requestMethod);
        }
    }

    public static Class<? extends Annotation> getRestMethodAnnotation(String requestUrl, String requestMethod, String pureEndingOfResource) {
        requestUrl = removeFromStringEndinSleshSimbol(requestUrl);
        switch (requestMethod) {
            case "GET":
                if (requestUrl.matches(pureEndingOfResource)) {
                    return RestGetAll.class;
                } else {
                    return RestGetById.class;
                }
            case "POST":
                return RestUpdate.class;
            case "PUT":
                return RestPut.class;
            case "DELETE":
                return RestDelete.class;
            default:
                throw new UnsupportedOperationException("there is no method for request " + requestUrl + "with method - " + requestMethod);
        }
    }

    private static String removeFromStringEndinSleshSimbol(String linkKey) {
        if (linkKey.lastIndexOf("/") == linkKey.length() - 1) {
            linkKey = linkKey.substring(0, linkKey.length() - 1);
        }
        return linkKey;
    }
}
