package see.tree;

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
}
