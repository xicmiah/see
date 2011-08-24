package see.tree;

public interface Node<T> {
	T accept(Visitor visitor);
}
