package see.evaluator;

import com.google.common.base.Suppliers;
import see.exceptions.EvaluationException;
import see.parser.numbers.NumberFactory;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableList.of;

public class SimpleEvaluator implements Evaluator {

    private final NumberFactory numberFactory;

    public SimpleEvaluator(NumberFactory numberFactory) {
        this.numberFactory = numberFactory;
    }

    /**
     * Evaluate tree with exception translation.
     * Subclasses of [@link EvaluationException] are passed as-is, others are wrapped in [@link EvaluationException].
     * 
     * @param tree tree to evaluate
     * @param context evaluation context
     * @param <T> return type
     * @return evaluation result
     * @throws EvaluationException on error during evaluation
     */
    @Override
    public <T> T evaluate(Node<T> tree, Map<String, ?> context) throws EvaluationException {
        try {
            ValueProcessor numberLifter = new NumberLifter(Suppliers.ofInstance(numberFactory));
            return tree.accept(new LazyVisitor(context, of(numberLifter)));
        } catch (EvaluationException e) {
            throw e;
        } catch (Exception e) {
            throw new EvaluationException(e);
        }
    }
}
