package see.functions;

import com.google.common.base.Function;

import java.util.Map;

/**
 * Wrapper for pure functions, which throws away context
 * @param <F> wrapped function
 */
public class PureFunction<F extends Function<?, ?>> implements ContextCurriedFunction<F> {
    private final F delegate;

    public PureFunction(F delegate) {
        this.delegate = delegate;
    }

    @Override
    public F apply(Map<String, Object> context) {
        return delegate;
    }
}
