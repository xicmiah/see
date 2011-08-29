package see.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.SuppressNode;
import see.tree.Node;

public abstract class MacroGrammar extends BaseParser<Node<Number>> {
    public static final String WHITESPACE = " \t\f\r\n";

    public Rule CalcExpression() {
        return Sequence(ExpressionList(), "return", RightExpression(), EOI);
    }

    public Rule Condition() {
        return Sequence(RightExpression(), EOI);
    }

    Rule ExpressionList() {
        return Sequence(Expression(), ZeroOrMore(";", Optional(Expression())));
    }

    Rule Expression() {
        return FirstOf(AssignExpression(), Conditional(), RightExpression());
    }

    Rule AssignExpression() {
        return Sequence(Variable(), ";", Expression());
    }

    Rule Conditional() {
        return Sequence("if", "(", RightExpression(), ")",
                "then", "{", ExpressionList(), "}",
                Optional("else", "{", ExpressionList(), "}"));
    }

    Rule Variable() {
        return Identifier();
    }

    abstract Rule RightExpression();

    abstract Rule Identifier();

    @SuppressNode
    Rule Whitespace() {
        return ZeroOrMore(AnyOf(WHITESPACE));
    }

    @Override
    protected Rule fromStringLiteral(String string) {
        return Sequence(String(string.trim()), Whitespace());
    }

    /**
     * Repeat expression with separator. Expression must match at least once.
     * Corresponds to (rule (separator rule)*)
     *
     * @param rule expression to repeat
     * @param separator separator between repeats of rule
     * @return resulting rule
     */
    Rule rep1sep(Object rule, Object separator) {
        return Sequence(rule, ZeroOrMore(separator, rule));
    }

    /**
     * Repeat expression with separator. Expression can match zero times.
     * Corresponds to (rule (separator rule)*)?
     *
     * @param rule expression to repeat
     * @param separator separator between repeats of rule
     * @return resulting rule
     */
    Rule repsep(Object rule, Object separator) {
        return Optional(rep1sep(rule, separator));
    }
}
