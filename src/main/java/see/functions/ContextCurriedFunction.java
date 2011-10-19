package see.functions;

import java.util.Map;

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
public interface ContextCurriedFunction<F extends Function<?, ?>> extends Function<Map<String, ?>, F> {
    @Override
    F apply(final Map<String, ?> context);
}
