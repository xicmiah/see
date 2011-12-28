package see.functions;

import see.evaluation.Context;

import javax.annotation.Nonnull;

/**
 * Wrapper for pure functions, which throws away context
 * @param <Arg> function argument type
 * @param <Result> function result type
 */
public class PureFunction<Arg, Result> implements ContextCurriedFunction<Arg, Result> {
    private final VarArgFunction<Arg, Result> delegate;

    public PureFunction(VarArgFunction<Arg, Result> delegate) {
        this.delegate = delegate;
    }

    public static <A, R> PureFunction<A, R> wrap(VarArgFunction<A, R> delegate) {
        return new PureFunction<A, R>(delegate);
    }

    @Override
    public VarArgFunction<Arg, Result> apply(@Nonnull Context context) {
        return delegate;
    }

    @Override
    public String toString() {
        return String.valueOf(delegate);
    }
}
