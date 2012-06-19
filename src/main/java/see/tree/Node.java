package see.tree;

public interface Node<T> {
	T accept(Visitor visitor);

    <V> V accept(ValueVisitor<V> visitor);
}
