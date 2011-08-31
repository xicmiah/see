package see.tree;

import com.google.common.base.Objects;

public final class ConstNode<T> implements Node<T> {
	private final T value;

	public ConstNode(T value) {
		this.value = value;
	}

	public T accept(Visitor visitor) {
		return visitor.visit(this);
	}

	public T getValue() {
		return value;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstNode that = (ConstNode) o;

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
