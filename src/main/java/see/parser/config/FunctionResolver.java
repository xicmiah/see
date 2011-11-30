package see.parser.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import see.functions.ContextCurriedFunction;
import see.functions.Function;

import java.util.List;
import java.util.Map;

public class FunctionResolver {
    private final Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions;
    private final Map<String, String> aliases;

    public FunctionResolver(Map<String, ? extends ContextCurriedFunction<Function<List<Object>, Object>>> functions,
                            Map<String, String> aliases) {
        this.functions = ImmutableMap.copyOf(functions);
        this.aliases = ImmutableMap.copyOf(aliases);
    }

    /**
     * Get function by name
     * @param name function name
     * @return corresponding function
     */
    public ContextCurriedFunction<Function<List<Object>, Object>> get(String name) {
        if (aliases.containsKey(name)) {
            return functions.get(aliases.get(name));
        } else {
            return functions.get(name);
        }
    }

    /**
     * Get contained functions bound to supplied context
     * @param context target context
     * @return mapping name -> bound function
     */
    public Map<String, Function<List<Object>, Object>> getBoundFunctions(Map<String, Object> context) {
        Map<String, Function<List<Object>, Object>> bound = Maps.newHashMap();
        for (String function : functions.keySet()) {
            bound.put(function, get(function).apply(context));
        }

        for (String alias : aliases.keySet()) {
            bound.put(alias, get(alias).apply(context));
        }

        return bound;
    }

}
