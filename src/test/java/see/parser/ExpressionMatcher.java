package see.parser;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.evaluator.BigDecimalFactory;
import see.parser.grammar.Expressions;
import see.parser.grammar.FunctionResolver;

/**
 * Matcher for expressions
 */
public class ExpressionMatcher extends TypeSafeMatcher<String> {
    private final ParseRunner<?> runner;
    private final String description;

    public ExpressionMatcher(Rule rule, String description) {
        this.runner = new ReportingParseRunner<Object>(rule);
        this.description = description;
    }

    @Override
    public boolean matchesSafely(String item) {
        ParsingResult<?> result = runner.run(item);
        return result.matched;
    }

    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    private static Expressions getDefaultGrammar() {
        GrammarConfiguration config = new GrammarConfiguration(new FunctionResolver(), new BigDecimalFactory());
        return Parboiled.createParser(Expressions.class, config);
    }

    @Factory
    public static Matcher<String> singleExpression() {
        return new ExpressionMatcher(getDefaultGrammar().Condition(), "a single expression");
    }

    @Factory
    public static Matcher<String> returnExpression() {
        return new ExpressionMatcher(getDefaultGrammar().CalcExpression(), "an expression");
    }

    @Factory
    public static Matcher<String> expressionList() {
        return new ExpressionMatcher(getDefaultGrammar().Statements(), "a list of expressions");
    }
}