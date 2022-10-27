package infrastructure.util;

import infrastructure.RestProcessorMethodsInfo;
import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.RestUrlVariableInfo;
import infrastructure.anotation.rest.RestDelete;
import infrastructure.anotation.rest.RestGetAll;
import infrastructure.anotation.rest.RestGetById;
import infrastructure.anotation.rest.RestPut;
import infrastructure.anotation.rest.RestUpdate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class RestUrlUtilService {

    public static Object[] retrieveParametersFromRestUrl(String logicUrlPath, RestUrlCommandProcessorInfo restCommandProcessorInfo) {
        String[] urlSteps = logicUrlPath.split("\\/");
        Parameter[] parametersReflection = restCommandProcessorInfo.getProcessorsMethod().getParameters();
        Object[] parametersToPassInInvocation = new Object[parametersReflection.length];
        for (int i = 0; i < parametersReflection.length; i++) {
            Parameter parameter = parametersReflection[i];
            List<RestUrlVariableInfo> restUrlVariableInfos = restCommandProcessorInfo.getRestUrlVariableInfos();
            for (int j = 0; j < restUrlVariableInfos.size(); j++) {
                if (restUrlVariableInfos.get(j).getKey().equals(parameter.getName())) {
                    parametersToPassInInvocation[i] = urlSteps[restUrlVariableInfos.get(j).getNumberStepInUrl()];
                }
            }
        }
        return parametersToPassInInvocation;
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
