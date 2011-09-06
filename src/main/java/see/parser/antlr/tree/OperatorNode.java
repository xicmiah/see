package see.parser.antlr.tree;

import see.functions.Function;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import see.functions.ContextCurriedFunction;
import see.tree.Node;

import java.util.List;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class OperatorNode<Arg, Result> extends AbstractFunctionNode<Arg,Result> {

    private final ContextCurriedFunction<Function<List<Arg>, Result>> function;

    public OperatorNode(Token token, ContextCurriedFunction<Function<List<Arg>, Result>> function) {
        super(token);
        this.function = function;
    }

    @Override
    public ContextCurriedFunction<Function<List<Arg>, Result>> getFunction() {
        return function;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Node<Arg>> getArguments() {
        return (List<Node<Arg>>) getChildren();
    }

    @Override
    public Tree dupNode() {
        return new OperatorNode<Arg,Result>(getToken(), function);
    }
}
