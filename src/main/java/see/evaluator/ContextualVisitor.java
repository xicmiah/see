package see.evaluator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import see.functions.Function;
import see.parser.numbers.NumberFactory;
import see.tree.*;

import java.util.List;
import java.util.Map;

public class ContextualVisitor implements Visitor {
	private final Map<String, Object> context;
    private final NumberFactory numberFactory;

	public ContextualVisitor(NumberFactory numberFactory, Map<String, ?> context) {
        this.numberFactory = numberFactory;
        this.context = Maps.newHashMap(context);
	}

	@Override
    public <Arg, Result> Result visit(FunctionNode<Arg, Result> node) {
		List<Arg> evaluatedArgs = Lists.transform(node.getArguments(), new Function<Node<Arg>, Arg>() {
			@Override
            public Arg apply(Node<Arg> input) {
				return input.accept(ContextualVisitor.this);
			}
		});

		// Note: evaluatedArgs are lazy
        Function<List<Arg>, Result> partial = node.getFunction().apply(context);
        Result result = partial.apply(evaluatedArgs);

        return passThroughNumberFactory(result);
	}

    /**
     * Get variable value from context.
     * If context holds a Number, the value is passed through NumberFactory,
     * otherwise it's returned directly.
     *
     * @param node visited node
     * @param <T> node type
     * @return extracted value
     */
	@Override
    @SuppressWarnings("unchecked")
    public <T> T visit(VarNode<T> node) {
        Object value = context.get(node.getName());

        return (T) passThroughNumberFactory(value);
	}

	@Override
    public <T> T visit(ConstNode<T> node) {
		return node.getValue();
	}

    /**
     * Pass value through number factory if value is number, return it otherwise.
     * @param value value to pass
     * @param <T> value type
     * @return converted value
     */
    private <T> T passThroughNumberFactory(T value) {
        if (value instanceof Number) {
            return (T) numberFactory.getNumber((Number) value);
        } else {
            return value;
        }
    }
}
