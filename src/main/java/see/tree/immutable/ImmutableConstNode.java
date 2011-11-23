package see.tree.immutable;

import com.google.common.base.Objects;
import see.tree.ConstNode;
import see.tree.Visitor;

public final class ImmutableConstNode<T> implements ConstNode<T> {
	private final T value;

	public ImmutableConstNode(T value) {
		this.value = value;
	}

	@Override
    public T accept(Visitor visitor) {
		return visitor.visit(this);
	}

    @Override
    public <V> V accept(ValueVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override public T getValue() {
		return value;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableConstNode that = (ImmutableConstNode) o;

        return Objects.equal(value, that.value);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
