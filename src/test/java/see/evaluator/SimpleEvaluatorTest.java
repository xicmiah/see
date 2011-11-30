package see.evaluator;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.exceptions.EvaluationException;
import see.functions.Function;
import see.functions.PureFunction;
import see.functions.VarArgFunction;
import see.parser.config.ConfigBuilder;
import see.parser.numbers.BigDecimalFactory;
import see.tree.Node;
import see.tree.immutable.ImmutableFunctionNode;

import javax.annotation.Nonnull;
import java.util.List;

public class SimpleEvaluatorTest {

    Evaluator evaluator = new SimpleEvaluator(new BigDecimalFactory(), ConfigBuilder.emptyConfig().build().getFunctions());
    
    final VarArgFunction<Object, Object> epicFail = new VarArgFunction<Object, Object>() {
        @Override
        public Object apply(@Nonnull List<Object> input) {
            throw new EpicFailException();
        }
    };

    final VarArgFunction<Object, Object> fail = new VarArgFunction<Object, Object>() {
        @Override
        public Object apply(@Nonnull List<Object> input) {
            throw new RuntimeException();
        }
    };

    /**
     * Test that all runtime exceptions are wrapped in EvaluationException
     * @throws Exception
     */
    @Test(expected = EvaluationException.class)
    public void testExceptionTranslationForRuntime() throws Exception {
        Node<Object> tree = new ImmutableFunctionNode<Object, Object>(new PureFunction<Function<List<Object>, Object>>(fail));

        evaluator.evaluate(tree, ImmutableMap.<String, Object>of());
    }

    /**
     * Test that subclasses of EvaluationException are not wrapped
     * @throws Exception
     */
    @Test(expected = EpicFailException.class)
    public void testExceptionTranslation() throws Exception {
        PureFunction<Function<List<Object>, Object>> pureFail = new PureFunction<Function<List<Object>, Object>>(epicFail);
        Node<Object> tree = new ImmutableFunctionNode<Object, Object>(pureFail);

        evaluator.evaluate(tree, ImmutableMap.<String, Object>of());
    }

    private static class EpicFailException extends EvaluationException {
        public EpicFailException() {
            super("It failed");
        }
    }
}
