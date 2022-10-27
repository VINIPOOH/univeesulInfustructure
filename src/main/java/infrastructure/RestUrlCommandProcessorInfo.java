package infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestUrlCommandProcessorInfo {
    private List<RestUrlVariableInfo> restUrlVariableInfos;
    private Object commandProcessor;
    private Method processorsMethod;
}
