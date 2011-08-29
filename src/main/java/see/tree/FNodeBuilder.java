package see.tree;

import com.google.common.base.Function;
import see.functions.ContextCurriedFunction;
import see.functions.PureFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for Function nodes
 * @param <Arg> argument type
 * @param <Result> result type
 */
public class FNodeBuilder<Arg, Result> {
    private ContextCurriedFunction<Function<List<Arg>, Result>> function;
    private List<Node<Arg>> args = new ArrayList<Node<Arg>>();

    /**
     * Constructor from context-aware function
     * @param function function to add to node
     */
    public FNodeBuilder(ContextCurriedFunction<Function<List<Arg>, Result>> function) {
        this.function = function;
    }

    /**
     * Constructor from pure function
     * @param function function to add to node
     */
    public FNodeBuilder(Function<List<Arg>, Result> function) {
        this.function = new PureFunction<Function<List<Arg>, Result>>(function);
    }

    public FunctionNode<Arg, Result> build() {
        return new FunctionNode<Arg, Result>(function, args);
    }

    public FNodeBuilder<Arg, Result> addArg(Node<Arg> argument) {
        args.add(argument);
        return this;
    }

    public FNodeBuilder<Arg, Result> addArgs(Node<Arg>... arguments) {
        args.addAll(args);
        return this;
    }
}
