package see.tree;

import com.google.common.base.Function;
import see.functions.ContextCurriedFunction;

import java.util.List;

/**
 * @author pavlov
 * @since 05.09.11
 */
public interface FunctionNode<Arg, Result> extends Node<Result> {
    ContextCurriedFunction<Function<List<Arg>, Result>> getFunction();

    List<Node<Arg>> getArguments();
}
