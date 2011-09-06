package see.evaluator;

import see.exceptions.EvaluationException;
import see.parser.numbers.NumberFactory;
import see.tree.Node;

import java.util.Map;

public class SimpleEvaluator implements Evaluator {

    private final NumberFactory numberFactory;

    public SimpleEvaluator(NumberFactory numberFactory) {
        this.numberFactory = numberFactory;
    }
    
    @Override
    public <T> T evaluate(Node<T> tree, Map<String, ?> context) throws EvaluationException {
        try {
            return tree.accept(new ContextualVisitor(numberFactory, context));
        } catch (Exception e) {
            throw new EvaluationException(e);
        }
    }
}
