package see.parser;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import see.functions.ContextCurriedFunction;
import see.functions.PureFunction;

import java.util.List;
import java.util.Map;

public class FunctionResolver {

    /**
     * Function table stub
     * Returns PureFunction wrapping null, with toString() returning function name
     */
    // TODO: add proper function resolution
    private final Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> funTab;

    public FunctionResolver() {
        funTab = new MapMaker().makeComputingMap(new Function<String, ContextCurriedFunction<Function<List<Object>, Object>>>() {
            @Override
            public ContextCurriedFunction<Function<List<Object>, Object>> apply(final String name) {
                return new PureFunction<Function<List<Object>, Object>>(null) {
                    @Override
                    public String toString() {
                        return name;
                    }
                };
            }
        });
    }

    /**
     * Get function by name
     * @param name function name
     * @return corresponding function
     */
    public ContextCurriedFunction<Function<List<Object>, Object>> get(String name) {
        return funTab.get(name);
    }

    /**
     * Get operator by name
     * @param operator operator name
     * @return corresponding operator function
     */
    public ContextCurriedFunction<Function<List<Object>, Object>> getOp(String operator) {
        return funTab.get(operator);
    }
}
