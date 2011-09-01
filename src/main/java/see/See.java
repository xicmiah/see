package see;

import org.parboiled.Parboiled;
import org.parboiled.Rule;
import see.evaluator.BigDecimalFactory;
import see.evaluator.SimpleEvaluator;
import see.parser.BasicParser;
import see.parser.GrammarConfiguration;
import see.parser.Parser;
import see.parser.grammar.Expressions;
import see.parser.grammar.FunctionResolver;
import see.tree.Node;

import java.util.Map;

/**
 * Facade to parse/evaluate operations.
 * All operations are thread-safe. Parse results are immutable and can be reused/cached between different instances.
 */
public class See {
    /**
     * Parse a single expression
     *
     * @param expression text to parse
     * @return parsed tree
     */
    public Node<Object> parseExpression(String expression) {
        return parse(expression, getGrammar().Condition());
    }

    /**
     * Parse semicolon-separated list of expressions
     *
     * @param expression text to parse
     * @return parsed tree
     */
    public Node<Object> parseExpressionList(String expression) {
        return parse(expression, getGrammar().Statements());
    }

    /**
     * Parse a expression with 'return' keyword
     *
     * @param expression text to parse
     * @return parsed tree
     */
    public Node<Object> parseReturnExpression(String expression) {
        return parse(expression, getGrammar().CalcExpression());
    }

    /**
     * Evaluate tree with supplied variables
     *
     * @param tree tree to evaluate
     * @param context variable->value mapping
     * @param <T> return type
     * @return evaluated value
     */
    public <T> T evaluate(Node<T> tree, Map<String, Object> context) {
        return new SimpleEvaluator().evaluate(tree, context);
    }

    private Node<Object> parse(String expression, Rule rule) {
        Parser<Object> parser = new BasicParser<Object>(rule);
        return parser.parse(expression);
    }

    private Expressions getGrammar() {
        return Parboiled.createParser(Expressions.class, getDefaultConfig());
    }

    private GrammarConfiguration getDefaultConfig() {
        return new GrammarConfiguration(new FunctionResolver(), new BigDecimalFactory());
    }
}
