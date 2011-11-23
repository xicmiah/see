package see.tree;

public interface Node<T> {
	T accept(Visitor visitor);

    <V> V accept(ValueVisitor<V> visitor);

    /**
     * Visitor with arbitrary return type.
     * @param <T> return type
     */
    interface ValueVisitor<T> {
        T visit(ConstNode<?> constNode);

        T visit(VarNode<?> varNode);

        T visit(FunctionNode<?, ?> functionNode);

        T visit(PropertyNode<?> propertyNode);
    }
}
