package see.parser.grammar;

import com.google.common.base.Function;
import see.functions.ContextCurriedFunction;

import java.util.List;

/**
 * A curried untyped function.
 * Serves as shortcut for long generic type declaration
 */
interface UntypedFunction extends ContextCurriedFunction<Function<List<Object>, Object>> {
}
