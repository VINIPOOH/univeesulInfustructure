package infrastructure;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class RestProcessorCashInfo {
    private List<RestUrlVariableInfo> restUrlVariableInfos;
    private Class<?> commandProcessor;
    private Method getByIdMethod;
    private Method getAllMethod;
    private Method putMethod;
    private Method deleteMethod;
    private Method updateMethod;
}
