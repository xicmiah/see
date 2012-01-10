package see.parser.config;

import com.google.common.collect.ImmutableMap;
import see.functions.ContextCurriedFunction;

import java.util.Map;

import static com.google.common.base.Functions.forMap;
import static com.google.common.collect.Maps.transformValues;

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
        ImmutableMap.Builder<String, ContextCurriedFunction<Object, Object>> builder = ImmutableMap.builder();
        return builder
                .putAll(functions)
                .putAll(transformValues(aliases, forMap(functions)))
                .build();
    }
}
