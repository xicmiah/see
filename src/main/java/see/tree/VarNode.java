package see.tree;

import com.google.common.base.Objects;

public final class VarNode<T> implements Node<T> {
	private final String name;

	public VarNode(String name) {
		this.name = name;
	}

	public T accept(Visitor visitor) {
		return visitor.visit(this);
	}

	public String getName() {
		return name;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VarNode that = (VarNode) o;

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
