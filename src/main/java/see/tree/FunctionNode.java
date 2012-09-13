package see.tree;

import java.util.List;

/**
 * @author pavlov
 * @since 05.09.11
 */
public interface FunctionNode<Arg, Result> extends Node<Result> {
    String getFunctionName();

    List<Node<Arg>> getArguments();
}
