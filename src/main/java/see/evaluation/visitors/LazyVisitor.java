package see.evaluation.visitors;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import see.evaluation.ValueProcessor;
import see.properties.ChainResolver;
import see.tree.Node;

import java.util.List;
import java.util.Map;

public class LazyVisitor extends AbstractVisitor {

    /**
     * Create a visitor from initial context and list of post-processors.
     *
     * @param context initial context
     * @param valueProcessors value processors
     * @param resolver property chain resolver
     */
    public LazyVisitor(Map<String, ?> context,
                       List<ValueProcessor> valueProcessors, ChainResolver resolver) {
        super(context, valueProcessors, resolver);
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
