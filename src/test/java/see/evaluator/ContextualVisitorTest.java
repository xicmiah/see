package see.evaluator;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContextualVisitorTest {
	@Test
	public void testEvaluation() throws Exception {
		Function<List<Integer>, Integer> plus = new Function<List<Integer>, Integer>() {
			public Integer apply(List<Integer> input) {
				int result = 0;
				for (Integer value : input) {
					result += value;
				}
				return result;
			}
		};
		List<Node<Integer>> arguments = ImmutableList.<Node<Integer>>of(new ConstNode<Integer>(1), new ConstNode<Integer>(2));
		Node<Integer> tree = new FunctionNode<Integer, Integer>(plus, arguments);

		int result = new SimpleEvaluator().evaluate(tree, new HashMap<String, Object>());

		assertEquals(3, result);
	}

	@Test
	public void testLazy() throws Exception {
		Function<List<Integer>, Integer> fail = new Function<List<Integer>, Integer>() {
			@Override
			public Integer apply(List<Integer> input) {
				throw new UnsupportedOperationException("Fail");
			}
		};

		Function<List<Integer>, Integer> cond = new Function<List<Integer>, Integer>() {
			@Override
			public Integer apply(List<Integer> input) {
				if (input.get(0) != 0) {
					return input.get(1);
				} else {
					return input.get(2);
				}
			}
		};

		FunctionNode<Integer, Integer> failNode = new FunctionNode<Integer, Integer>(fail);
		Node<Integer> tree = new FunctionNode<Integer, Integer>(cond,
				new ConstNode<Integer>(1),
				new ConstNode<Integer>(42),
				failNode);

		int result = new SimpleEvaluator().evaluate(tree, Collections.<String, Object>emptyMap());
		assertEquals(42, result);
	}
}
