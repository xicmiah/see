package see.evaluation.evaluators;

import com.google.common.collect.ClassToInstanceMap;
import see.evaluation.*;
import see.evaluation.conversions.BuiltinConversions;
import see.exceptions.EvaluationException;
import see.exceptions.PropagatedException;
import see.exceptions.SeeRuntimeException;
import see.parser.config.FunctionResolver;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableClassToInstanceMap.builder;
import static see.evaluation.scopes.Scopes.*;

public class SimpleEvaluator implements Evaluator {

    private final Scope functionScope;
    private final ClassToInstanceMap<Object> services;

    public SimpleEvaluator(Scope initialScope, ClassToInstanceMap<Object> services) {
        this.functionScope = initialScope;
        this.services = services;
    }

    public static Evaluator fromConfig(GrammarConfiguration config) {
        return new SimpleEvaluator(
                extractScope(config),
                extractServices(config)
        );
    }

    public static Scope extractScope(GrammarConfiguration config) {
        return fromMap(config.getFunctions().getFunctions());
    }

    public static ClassToInstanceMap<Object> extractServices(GrammarConfiguration config) {
        return builder()
                .put(NumberFactory.class, config.getNumberFactory())
                .put(ChainResolver.class, config.getChainResolver())
                .put(ValueProcessor.class, config.getValueProcessor())
                .put(ToFunction.class, BuiltinConversions.all())
                .put(FunctionResolver.class, config.getFunctions())
                .build();
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
            Context context = SimpleContext.create(createLocalScope(initial), services);

            return new LazyContextEvaluator().evaluate(tree, context);
        } catch (PropagatedException e) {
            throw new SeeRuntimeException(SeeRuntimeException.getTrace(e), e.getLastCause());
        } catch (Exception e) {
            throw new SeeRuntimeException(SeeRuntimeException.getTrace(e), e);
        }
    }

    private Scope createLocalScope(Map<String, ?> initial) {
        return defCapture(mutableOverride(functionScope, initial));
    }
}
