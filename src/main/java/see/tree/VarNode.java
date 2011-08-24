package see.tree;

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

}
