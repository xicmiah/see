package see.evaluator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import see.tree.BlockNode;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.VarNode;
import see.tree.Visitor;

import java.util.List;
import java.util.Map;

public class ContextualVisitor implements Visitor {
	private final Map<String, Object> context;

	public ContextualVisitor(Map<String, Object> context) {
		this.context = Maps.newHashMap(context);
	}

	public <T> T visit(BlockNode<T> node) {
		for (Node<?> statement : node.init()) {
			statement.accept(this);
		}

		return node.last().accept(this);
	}

	public <Arg, Result> Result visit(FunctionNode<Arg, Result> node) {
		List<Arg> evaluatedArgs = Lists.transform(node.getArguments(), new Function<Node<Arg>, Arg>() {
			public Arg apply(Node<Arg> input) {
				return input.accept(ContextualVisitor.this);
			}
		});

		// Note: evaluatedArgs are lazy
		return node.getFunction().apply(evaluatedArgs);
	}

	public <T> T visit(VarNode<T> node) {
		return (T) context.get(node.getName());
	}

	public <T> T visit(ConstNode<T> node) {
		return node.getValue();
	}
}
