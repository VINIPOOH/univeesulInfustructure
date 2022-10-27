package infrastructure;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class RestProcessorMethodsInfo {
    private List<RestUrlVariableInfo> restUrlVariableInfos;
    private String pureEndingOfResource;
    private Object commandProcessor;
    private Method getByIdMethod;
    private Method getAllMethod;
    private Method putMethod;
    private Method deleteMethod;
    private Method updateMethod;
}
