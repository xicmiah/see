package see.evaluation;

import see.exceptions.EvaluationException;
import see.tree.Node;

import java.util.Map;

public interface Evaluator {
	<T> T evaluate(Node<T> tree, Map<String, ?> context) throws EvaluationException;
}
