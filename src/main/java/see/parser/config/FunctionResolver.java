package see.parser.config;

import com.google.common.collect.ImmutableMap;
import see.functions.ContextCurriedFunction;

import java.util.Map;

public class FunctionResolver {
    private final Map<String, ContextCurriedFunction<Object, Object>> functions;
    private final Map<String, String> aliases;

    public FunctionResolver(Map<String, ? extends ContextCurriedFunction<Object, Object>> functions,
                            Map<String, String> aliases) {
        this.functions = ImmutableMap.copyOf(functions);
        this.aliases = ImmutableMap.copyOf(aliases);
    }

    /**
     * Get function by name
     * @param name function name
     * @return corresponding function
     */
    public ContextCurriedFunction<Object, Object> get(String name) {
        if (aliases.containsKey(name)) {
            return functions.get(aliases.get(name));
        } else {
            return functions.get(name);
        }
    }

    public Map<String, ContextCurriedFunction<Object, Object>> getFunctions() {
        return functions;
    }
}
