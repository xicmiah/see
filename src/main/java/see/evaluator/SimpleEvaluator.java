package see.evaluator;

import com.google.common.base.Suppliers;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;
import see.exceptions.EvaluationException;
import see.functions.properties.PropertyUtilsResolver;
import see.functions.properties.SingularChainResolver;
import see.parser.config.FunctionResolver;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableList.of;

public class SimpleEvaluator implements Evaluator {

    private final NumberFactory numberFactory;
    private final FunctionResolver functionResolver;

    public SimpleEvaluator(NumberFactory numberFactory, FunctionResolver functionResolver) {
        this.numberFactory = numberFactory;
        this.functionResolver = functionResolver;
    }

    public static SimpleEvaluator fromConfig(GrammarConfiguration config) {
        return new SimpleEvaluator(config.getNumberFactory(), config.getFunctions());
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

            Map<String, Object> extendedContext = getExtendedContext(context);
            
            return tree.accept(new LazyVisitor(extendedContext, of(numberLifter), new SingularChainResolver(new PropertyUtilsResolver())));
        } catch (EvaluationException e) {
            throw e;
        } catch (Exception e) {
            throw new EvaluationException(e);
        }
    }

    private Map<String, Object> getExtendedContext(Map<String, ?> context) {
        @SuppressWarnings("unchecked")
        final Map<String, Object> writableContext = (Map<String, Object>) context;
        return new ForwardingMap<String, Object>() {
            @Override
            protected Map<String, Object> delegate() {
                Map<String, Object> initialContext = Maps.newHashMap(writableContext);
                initialContext.putAll(functionResolver.getBoundFunctions(initialContext));
                return initialContext;
            }

            @Override
            public Object put(String key, Object value) {
                writableContext.put(key, value);
                return super.put(key, value);
            }

            @Override
            public void putAll(Map<? extends String, ?> map) {
                standardPutAll(map);
            }
        };
    }
}
