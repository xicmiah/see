package see.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class BlockNode<T> implements Node<T> {
	private final List<Node<?>> statements;

	public BlockNode(List<Node<?>> statements) {
		this.statements = ImmutableList.copyOf(statements);
	}

	public T accept(Visitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * Get all elements except the last one
	 * @return
	 */
	public List<Node<?>> init() {
		return statements.subList(0, statements.size() - 1);
	}

	public Node<T> last() {
		return (Node<T>) statements.get(statements.size());
	}

	public List<Node<?>> getStatements() {
		return statements;
	}
}
