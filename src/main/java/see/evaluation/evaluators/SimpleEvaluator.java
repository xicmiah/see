package see.evaluation.evaluators;

import com.google.common.base.Suppliers;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MutableClassToInstanceMap;
import see.evaluation.Context;
import see.evaluation.Evaluator;
import see.evaluation.ValueProcessor;
import see.evaluation.processors.NumberLifter;
import see.evaluation.visitors.LazyVisitor;
import see.exceptions.EvaluationException;
import see.functions.Function;
import see.parser.config.FunctionResolver;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.tree.Node;

import java.util.List;
import java.util.Map;

public class SimpleEvaluator implements Evaluator {

    private final NumberFactory numberFactory;
    private final FunctionResolver functionResolver;
    private final ChainResolver chainResolver;

    public SimpleEvaluator(NumberFactory numberFactory, FunctionResolver functionResolver, ChainResolver chainResolver) {
        this.numberFactory = numberFactory;
        this.functionResolver = functionResolver;
        this.chainResolver = chainResolver;
    }

    public static SimpleEvaluator fromConfig(GrammarConfiguration config) {
        return new SimpleEvaluator(config.getNumberFactory(), config.getFunctions(), config.getChainResolver());
    }

    /**
     * Evaluate tree with exception translation.
     * Subclasses of [@link EvaluationException] are passed as-is, others are wrapped in [@link EvaluationException].
     * 
     * @param tree tree to evaluate
     * @param context evaluation context
     * @param <T> return type
     * @return evaluation result
     * @throws EvaluationException on error during evaluation
     */
    @Override
    public <T> T evaluate(Node<T> tree, final Map<String, ?> context) throws EvaluationException {
        try {
            ValueProcessor numberLifter = new NumberLifter(Suppliers.ofInstance(numberFactory));

            Context extendedContext = getExtendedContext(context);
            
            return tree.accept(new LazyVisitor(extendedContext, numberLifter, chainResolver));
        } catch (EvaluationException e) {
            throw e;
        } catch (Exception e) {
            throw new EvaluationException(e);
        }
    }

    private Context getExtendedContext(Map<String, ?> initial) {
        ClassToInstanceMap<Object> services = MutableClassToInstanceMap.create();
        services.putInstance(NumberFactory.class, numberFactory);
        services.putInstance(ChainResolver.class, chainResolver);

        ImmutableMap<String, Object> empty = ImmutableMap.of();
        SimpleContext context = new SimpleContext((Map<String, Object>) initial, empty, services);

        Map<String, Function<List<Object>, Object>> boundFunctions = functionResolver.getBoundFunctions(context);
        for (Map.Entry<String, Function<List<Object>, Object>> entry : boundFunctions.entrySet()) {
            context.addConstant(entry.getKey(), entry.getValue());
        }

        return context;
    }
}
