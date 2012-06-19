package see.tree.immutable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import see.functions.ContextCurriedFunction;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.ValueVisitor;
import see.tree.Visitor;

import java.util.List;

public final class ImmutableFunctionNode<Arg, Result> implements FunctionNode<Arg,Result> {
	private final ContextCurriedFunction<Arg, Result> function;
	private final List<Node<Arg>> arguments;

    public ImmutableFunctionNode(ContextCurriedFunction<Arg, Result> function, List<Node<Arg>> arguments) {
        this.function = function;
        this.arguments = ImmutableList.copyOf(arguments);
    }

    public ImmutableFunctionNode(ContextCurriedFunction<Arg, Result> function) {
        this.function = function;
        this.arguments = ImmutableList.of();
    }

    @Override
    public Result accept(Visitor visitor) {
		return visitor.visit(this);
	}

    @Override
    public <V> V accept(ValueVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ContextCurriedFunction<Arg, Result> getFunction() {
        return function;
    }

    @Override
    public List<Node<Arg>> getArguments() {
		return arguments;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableFunctionNode that = (ImmutableFunctionNode) o;

        return Objects.equal(function, that.function) && Objects.equal(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(function, arguments);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(function).append(arguments);
        return sb.toString();
    }
}
