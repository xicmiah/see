package see.evaluation.evaluators;

import com.google.common.collect.ClassToInstanceMap;
import see.evaluation.*;
import see.evaluation.conversions.BuiltinConversions;
import see.exceptions.EvaluationException;
import see.parser.config.FunctionResolver;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableClassToInstanceMap.builder;
import static see.evaluation.scopes.Scopes.*;

public class SimpleEvaluator implements Evaluator {

    private final NumberFactory numberFactory;
    private final FunctionResolver functionResolver;
    private final ChainResolver chainResolver;
    private final ValueProcessor valueProcessor;

    public SimpleEvaluator(NumberFactory numberFactory, FunctionResolver functionResolver, ChainResolver chainResolver, ValueProcessor valueProcessor) {
        this.numberFactory = numberFactory;
        this.functionResolver = functionResolver;
        this.chainResolver = chainResolver;
        this.valueProcessor = valueProcessor;
    }

    public static SimpleEvaluator fromConfig(GrammarConfiguration config) {
        return new SimpleEvaluator(
                config.getNumberFactory(),
                config.getFunctions(),
                config.getChainResolver(),
                config.getValueProcessor()
        );
    }

    /**
     * Evaluate tree with exception translation.
     * Subclasses of [@link EvaluationException] are passed as-is, others are wrapped in [@link EvaluationException].
     * 
     * @param tree tree to evaluate
     * @param initial evaluation context
     * @param <T> return type
     * @return evaluation result
     * @throws EvaluationException on error during evaluation
     */
    @Override
    public <T> T evaluate(Node<T> tree, final Map<String, ?> initial) throws EvaluationException {
        try {
            Context context = SimpleContext.create(createScope(initial), createServices());

            return new LazyContextEvaluator().evaluate(tree, context);
        } catch (EvaluationException e) {
            throw e;
        } catch (Exception e) {
            throw new EvaluationException(e);
        }
    }

    private Scope createScope(Map<String, ?> initial) {
        return defCapture(mutableOverride(fromMap(functionResolver.getFunctions()), initial));
    }

    private ClassToInstanceMap<Object> createServices() {
        return builder()
                .put(NumberFactory.class, numberFactory)
                .put(ChainResolver.class, chainResolver)
                .put(ValueProcessor.class, valueProcessor)
                .put(ToFunction.class, BuiltinConversions.all())
                .put(Evaluator.class, this)
                .build();
    }
}
