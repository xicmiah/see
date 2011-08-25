package see.evaluator;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.VarNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContextualVisitorTest {

    Function<List<Integer>,Integer> plus;
    Function<List<Integer>,Integer> fail;
    Function<List<Integer>,Integer> cond;
    
    Evaluator evaluator;

    @Before
    public void setUp() throws Exception {
        plus = new Function<List<Integer>, Integer>() {
            public Integer apply(List<Integer> input) {
                int result = 0;
                for (Integer value : input) {
                    result += value;
                }
                return result;
            }
        };
        fail = new Function<List<Integer>, Integer>() {
            @Override
            public Integer apply(List<Integer> input) {
                throw new UnsupportedOperationException("Fail");
            }
        };
        cond = new Function<List<Integer>, Integer>() {
			@Override
			public Integer apply(List<Integer> input) {
				if (input.get(0) != 0) {
					return input.get(1);
				} else {
					return input.get(2);
				}
			}
		};
        evaluator = new SimpleEvaluator();
    }

    @Test
	public void testEvaluation() throws Exception {
        List<Node<Integer>> arguments = ImmutableList.<Node<Integer>>of(new ConstNode<Integer>(1), new ConstNode<Integer>(2));
		Node<Integer> tree = new FunctionNode<Integer, Integer>(plus, arguments);

		int result = evaluator.evaluate(tree, new HashMap<String, Object>());

		assertEquals(3, result);
	}

	@Test
	public void testLazy() throws Exception {
        FunctionNode<Integer, Integer> failNode = new FunctionNode<Integer, Integer>(fail);
		Node<Integer> tree = new FunctionNode<Integer, Integer>(cond,
				new ConstNode<Integer>(1),
				new ConstNode<Integer>(42),
				failNode);

		int result = evaluator.evaluate(tree, Collections.<String, Object>emptyMap());
		assertEquals(42, result);
	}

    @Test
    public void testVariables() throws Exception {
        Node<Integer> tree = new FunctionNode<Integer, Integer>(plus, new VarNode<Integer>("a"), new ConstNode<Integer>(5));
        ImmutableMap<String, Object> context = ImmutableMap.<String, Object>of("a", 4);

        int result = evaluator.evaluate(tree, context);
        
        assertEquals(9, result);
    }
}
