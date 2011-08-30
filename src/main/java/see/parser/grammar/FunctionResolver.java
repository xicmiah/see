package see.parser.grammar;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

import java.util.List;
import java.util.Map;

public class FunctionResolver {

    /**
     * Function table stub
     * Returns PureFunction wrapping null, with toString() returning function name
     */
    // TODO: add proper function resolution
    private final Map<String, UntypedFunction> funTab;

    public FunctionResolver() {
        funTab = new MapMaker().makeComputingMap(new Function<String, UntypedFunction>() {
            @Override
            public UntypedFunction apply(final String name) {
                return new UntypedFunction() {
                    @Override
                    public Function<List<Object>, Object> apply(Map<String, Object> context) {
                        return null;
                    }

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
    public UntypedFunction get(String name) {
        return funTab.get(name);
    }

    /**
     * Get operator by name
     * @param operator operator name
     * @return corresponding operator function
     */
    public UntypedFunction getOp(String operator) {
        return funTab.get(operator);
    }
}
