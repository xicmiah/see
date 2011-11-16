package see.evaluator;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import see.tree.Node;

import java.util.List;
import java.util.Map;

public class LazyVisitor extends AbstractVisitor {

    public LazyVisitor(Map<String, ?> context) {
        this(context, ImmutableList.<ValueProcessor>of());
	}

    /**
     * Create a visitor from initial context and list of post-processors.
     *
     * @param context initial context
     * @param valueProcessors value processors
     */
    public LazyVisitor(Map<String, ?> context,
                       List<ValueProcessor> valueProcessors) {
        super(context, valueProcessors);
    }

    @Override
    protected <Arg> List<Arg> evaluateArgs(List<Node<Arg>> arguments) {
        return Lists.transform(arguments, new Function<Node<Arg>, Arg>() {
            @Override
            public Arg apply(Node<Arg> input) {
                return input.accept(LazyVisitor.this);
            }
        });
    }

}
