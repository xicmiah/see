package see.evaluator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import see.parser.numbers.NumberFactory;
import see.tree.*;

import java.util.List;
import java.util.Map;

public class ContextualVisitor implements Visitor {
	private final Map<String, Object> context;
    private final NumberFactory numberFactory;

	public ContextualVisitor(NumberFactory numberFactory, Map<String, Object> context) {
        this.numberFactory = numberFactory;
        this.context = Maps.newHashMap(context);
	}

	public <Arg, Result> Result visit(FunctionNode<Arg, Result> node) {
		List<Arg> evaluatedArgs = Lists.transform(node.getArguments(), new Function<Node<Arg>, Arg>() {
			public Arg apply(Node<Arg> input) {
				return input.accept(ContextualVisitor.this);
			}
		});

		// Note: evaluatedArgs are lazy
		return node.getFunction().apply(context).apply(evaluatedArgs);
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
	@SuppressWarnings("unchecked")
    public <T> T visit(VarNode<T> node) {
        Object value = context.get(node.getName());
        
        if (value instanceof Number) {
            return (T) numberFactory.getNumber((Number) value);
        } else {
            return (T) value;
        }
	}

	public <T> T visit(ConstNode<T> node) {
		return node.getValue();
	}
}
