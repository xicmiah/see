package see.functions;

import java.util.List;

/**
 * Alias to shorten declaration length.
 * @param <Arg> type of arguments
 * @param <Result> result type
 */
public interface VarArgFunction<Arg, Result> extends Function<List<Arg>, Result> {
}
