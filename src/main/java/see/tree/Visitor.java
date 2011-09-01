package see.tree;

public interface Visitor {
	<F, T> T visit(FunctionNode<F, T> node);
	<T> T visit(VarNode<T> node);
	<T> T visit(ConstNode<T> node);
}
