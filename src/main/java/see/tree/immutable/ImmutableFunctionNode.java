package see.tree.immutable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.ValueVisitor;
import see.tree.Visitor;

import java.util.List;

public final class ImmutableFunctionNode<Arg, Result> implements FunctionNode<Arg,Result> {
	private final String functionName;
	private final List<Node<Arg>> arguments;

    public ImmutableFunctionNode(String functionName) {
        this(functionName, ImmutableList.<Node<Arg>>of());
    }

    public ImmutableFunctionNode(String functionName, List<Node<Arg>> arguments) {
        this.functionName = functionName;
        this.arguments = ImmutableList.copyOf(arguments);
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
    public String getFunctionName() {
        return functionName;
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

        return Objects.equal(functionName, that.functionName) && Objects.equal(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(functionName, arguments);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(functionName).append(arguments);
        return sb.toString();
    }
}
