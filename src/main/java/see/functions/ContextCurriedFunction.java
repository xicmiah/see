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
 * @see PureFunction
 * @param <Arg> common argument supertype
 * @param <Result> result type
 */
public interface ContextCurriedFunction<Arg, Result> {
    VarArgFunction<Arg, Result> apply(@Nonnull final Context context);
}
