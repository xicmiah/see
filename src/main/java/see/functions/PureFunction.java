package see.functions;

import see.evaluation.Context;

import javax.annotation.Nonnull;

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
    public F apply(@Nonnull Context context) {
        return delegate;
    }

    @Override
    public String toString() {
        return String.valueOf(delegate);
    }
}
