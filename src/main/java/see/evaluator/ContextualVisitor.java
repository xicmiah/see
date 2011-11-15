package see.evaluator;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import see.tree.*;
import see.util.Reduce;

import java.util.List;
import java.util.Map;

import static see.util.Reduce.fold;

public class ContextualVisitor implements Visitor {
	private final Map<String, ?> context;

    private List<ValueProcessor> valueProcessors;

	public ContextualVisitor(Map<String, ?> context) {
        this(context, ImmutableList.<ValueProcessor>of());
	}

    /**
     * Create a visitor from initial context and list of post-processors.
     *
     * @param context initial context
     * @param valueProcessors value processors
     */
    public ContextualVisitor(Map<String, ?> context,
                             List<ValueProcessor> valueProcessors) {
        this.context = context;
        this.valueProcessors = valueProcessors;
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
        see.functions.Function<List<Arg>, Result> partial = node.getFunction().apply(context);
        Result result = partial.apply(evaluatedArgs);

        return processValue(result);
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

        return (T) processValue(value);
	}

	@Override
    public <T> T visit(ConstNode<T> node) {
		return node.getValue();
	}

    /**
     * Pass value through post-processors.
     * @param value
     * @param <T>
     * @return
     */
    private <T> T processValue(T value) {
        return (T) fold(value, valueProcessors, new Reduce.FoldFunction<Function<Object, Object>, Object>() {
            @Override
            public Object apply(Object prev, Function<Object, Object> arg) {
                return arg.apply(prev);
            }
        });
    }
}
