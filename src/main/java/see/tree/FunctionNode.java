package see.tree;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import see.functions.ContextCurriedFunction;

import java.util.List;

public final class FunctionNode<Arg, Result> implements Node<Result> {
	private final ContextCurriedFunction<Function<List<Arg>, Result>> function;
	private final List<Node<Arg>> arguments;

    public FunctionNode(ContextCurriedFunction<Function<List<Arg>, Result>> function, List<Node<Arg>> arguments) {
        this.function = function;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    public FunctionNode(ContextCurriedFunction<Function<List<Arg>, Result>> function) {
        this.function = function;
        this.arguments = ImmutableList.of();
    }

    public Result accept(Visitor visitor) {
		return visitor.visit(this);
	}

    public ContextCurriedFunction<Function<List<Arg>, Result>> getFunction() {
        return function;
    }

    public List<Node<Arg>> getArguments() {
		return arguments;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(function).append(arguments);
        return sb.toString();
    }
}
