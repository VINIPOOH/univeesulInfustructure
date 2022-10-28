package infrastructure.util;

import infrastructure.RestProcessorMethodsInfo;
import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.RestUrlVariableInfo;
import infrastructure.anotation.rest.RequestBody;
import infrastructure.anotation.rest.RestDelete;
import infrastructure.anotation.rest.RestGetAll;
import infrastructure.anotation.rest.RestGetById;
import infrastructure.anotation.rest.RestPut;
import infrastructure.anotation.rest.RestUpdate;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

public class RestUrlUtilService {

    @SneakyThrows
    public static Object[] retrieveParametersFromRestUrl(String logicUrlPath, RestUrlCommandProcessorInfo restCommandProcessorInfo,
                                                         HttpServletRequest request, HttpServletResponse response) {
        //todo refactor this method. It brakes single responsibility and abstraction principles. It should not retrieve parameters fom url and in the seme time retrieve body
        //todo in future allso will be neded to add awutowiring reuvest end response and this will also appere here .
        String[] urlSteps = logicUrlPath.split("\\/");
        Parameter[] parametersReflection = restCommandProcessorInfo.getProcessorsMethod().getParameters();
        Object[] parametersToReturn = new Object[parametersReflection.length];
        for (int i = 0; i < parametersReflection.length; i++) {
            Parameter parameter = parametersReflection[i];
            if (parameter.getType().equals(HttpServletRequest.class)) {
                parametersToReturn[i] = request;
                continue;
            } else if (parameter.getType().equals(HttpServletResponse.class)) {
                parametersToReturn[i] = response;
                continue;
            } else if (Arrays.stream(parameter.getAnnotations()).map(Annotation::annotationType).anyMatch(p -> p.equals(RequestBody.class))) {
                parametersToReturn[i] = new Gson()
                        .fromJson(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), parameter.getType());
                continue;
            }
            List<RestUrlVariableInfo> restUrlVariableInfos = restCommandProcessorInfo.getRestUrlVariableInfos();
            for (int j = 0; j < restUrlVariableInfos.size(); j++) {
                if (restUrlVariableInfos.get(j).getKey().equals(parameter.getName())) {
                    parametersToReturn[i] = urlSteps[restUrlVariableInfos.get(j).getNumberStepInUrl()];
                }
            }
        }
        return parametersToReturn;
    }

    public static Method retrieveMethodForProcessRequest(String requestUrl, String requestMethod, RestProcessorMethodsInfo restProcessorMethodsInfo) {
        switch (requestMethod) {
            case "GET":
                if (requestUrl.endsWith(restProcessorMethodsInfo.getPureEndingOfResource())) {
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
        }
        return null; //todo add processing default value
    }

    public static Class<? extends Annotation> getRestMethodAnnotation(String linkKey, String requestMethod, String pureEndingOfResource) {
        Class<? extends Annotation> restMethodAnnotation = null;
        switch (requestMethod) {
            case "GET":
                if (linkKey.endsWith(pureEndingOfResource)) {
                    restMethodAnnotation = RestGetAll.class;
                } else {
                    restMethodAnnotation = RestGetById.class;
                }
                break;
            case "POST":
                restMethodAnnotation = RestUpdate.class;
                break;
            case "PUT":
                restMethodAnnotation = RestPut.class;
                break;
            case "DELETE":
                restMethodAnnotation = RestDelete.class;
                break;
        }
        return restMethodAnnotation;
    }
}
