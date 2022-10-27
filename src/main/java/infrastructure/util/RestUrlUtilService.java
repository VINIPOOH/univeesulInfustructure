package infrastructure.util;

import infrastructure.RestUrlCommandProcessorInfo;
import infrastructure.RestUrlVariableInfo;

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
}
