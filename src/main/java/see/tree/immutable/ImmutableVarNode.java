package see.tree.immutable;

import com.google.common.base.Objects;
import see.tree.ValueVisitor;
import see.tree.VarNode;
import see.tree.Visitor;

public final class ImmutableVarNode<T> implements VarNode<T> {
	private final String name;

	public ImmutableVarNode(String name) {
		this.name = name;
	}

	@Override
    public T accept(Visitor visitor) {
		return visitor.visit(this);
	}

    @Override
    public <V> V accept(ValueVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override public String getName() {
		return name;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableVarNode that = (ImmutableVarNode) o;

        return Objects.equal(name, that.name);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return new StringBuilder("Var(").append(name).append(")").toString();
    }
}
