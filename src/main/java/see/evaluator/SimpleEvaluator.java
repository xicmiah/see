package see.evaluator;

import see.parser.numbers.NumberFactory;
import see.tree.Node;

import java.util.Map;

public class SimpleEvaluator implements Evaluator {

    private final NumberFactory numberFactory;

    public SimpleEvaluator(NumberFactory numberFactory) {
        this.numberFactory = numberFactory;
    }

    public <T> T evaluate(Node<T> tree, Map<String, Object> context) {
        return tree.accept(new ContextualVisitor(numberFactory, context));
	}
}
