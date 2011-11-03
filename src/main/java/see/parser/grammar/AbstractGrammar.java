package see.parser.grammar;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.SuppressNode;
import see.tree.Node;

abstract class AbstractGrammar extends BaseParser<Node<Object>> {
    public static final String WHITESPACE = " \t\f\r\n";

    @SuppressNode
    Rule Whitespace() {
        return ZeroOrMore(FirstOf(
                // whitespace
                OneOrMore(AnyOf(WHITESPACE)),

                // block comment
                Sequence("/*", ZeroOrMore(TestNot("*/"), ANY), "*/"),

                // end of line comment
                Sequence(
                        "//",
                        ZeroOrMore(TestNot(AnyOf("\r\n")), ANY),
                        FirstOf("\r\n", '\r', '\n', EOI)
                )
        ));
    }

    /**
     * Returns match() with removed trailing whitespace.
     * @return matched input text
     */
    protected String matchTrim() {
        return match().trim();
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

    /**
     * A Terminal symbol. Matches supplied rules and whitespace/comment after them.
     * @param children child rules to match
     * @return constructed rule
     */
    Rule T(Object... children) {
        return Sequence(Sequence(children), Whitespace());
    }
}
