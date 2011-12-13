package see.functions;

import see.evaluation.Context;

import javax.annotation.Nonnull;

/**
 * Interface for supporting context-aware functions.
 *
 * Instances of this type are curried functions, which accept context as their first argument
 * and return a function which accepts rest of arguments and returns result.
 *
 * TL;DR Context => [Args] => result
 * 
 * @param <F> curried remainder
 * @see PureFunction
 */
public interface ContextCurriedFunction<F extends Function<?, ?>> extends Function<Context, F> {
    @Override
    F apply(@Nonnull final Context context);
}
