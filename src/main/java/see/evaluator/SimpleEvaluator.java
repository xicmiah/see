package see.evaluator;

import see.tree.Node;

import java.util.Map;

public class SimpleEvaluator implements Evaluator {

	public <T> T evaluate(Node<T> tree, Map<String, Object> context) {
		return tree.accept(new ContextualVisitor(context));
	}
}
