package see.evaluation;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.evaluation.evaluators.SimpleEvaluator;
import see.exceptions.EvaluationException;
import see.exceptions.PropagatedException;
import see.functions.Function;
import see.functions.PureFunction;
import see.functions.VarArgFunction;
import see.parser.config.ConfigBuilder;
import see.tree.Node;
import see.tree.immutable.ImmutableFunctionNode;

import javax.annotation.Nonnull;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SimpleEvaluatorTest {

    Evaluator evaluator = SimpleEvaluator.fromConfig(ConfigBuilder.defaultConfig().build());
    
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
    @Test
    public void testExceptionTranslation() throws Exception {
        PureFunction<Function<List<Object>, Object>> pureFail = new PureFunction<Function<List<Object>, Object>>(epicFail);
        Node<Object> tree = new ImmutableFunctionNode<Object, Object>(pureFail);

        try {
            evaluator.evaluate(tree, ImmutableMap.<String, Object>of());
            fail("Exception expected");
        } catch (PropagatedException e) {
            assertThat(e.getLastCause(), instanceOf(EpicFailException.class));
        }
    }

    private static class EpicFailException extends EvaluationException {
        public EpicFailException() {
            super("It failed");
        }
    }
}
