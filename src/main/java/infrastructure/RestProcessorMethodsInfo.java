package infrastructure;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class RestProcessorMethodsInfo {
    private List<RestUrlVariableInfo> restUrlVariableInfos;
    private String pureEndingOfResource;
    private Class<?> commandProcessor;//todo consider Yse type here so be able to chash type of procesor for request anyway and retrive later just object from factory
    private Method getByIdMethod;
    private Method getAllMethod;
    private Method putMethod;
    private Method deleteMethod;
    private Method updateMethod;
}
