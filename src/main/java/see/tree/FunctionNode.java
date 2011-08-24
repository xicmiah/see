package see.tree;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import java.util.List;

public final class FunctionNode<Arg, Result> implements Node<Result> {
	private final Function<List<Arg>, Result> function;
	private final List<Node<Arg>> arguments;

	public FunctionNode(Function<List<Arg>, Result> function, List<Node<Arg>> arguments) {
		this.function = function;
		this.arguments = ImmutableList.copyOf(arguments);
	}

	public FunctionNode(Function<List<Arg>, Result> function, Node<Arg>... arguments) {
		this.function = function;
		this.arguments = ImmutableList.copyOf(arguments);
	}

	public FunctionNode(Function<List<Arg>, Result> function) {
		this.function = function;
		this.arguments = ImmutableList.of();
	}

	public Result accept(Visitor visitor) {
		return visitor.visit(this);
	}

	public Function<List<Arg>, Result> getFunction() {
		return function;
	}

	public List<Node<Arg>> getArguments() {
		return arguments;
	}

}
