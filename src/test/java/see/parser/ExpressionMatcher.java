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
import see.parser.config.ConfigBuilder;
import see.parser.grammar.Expressions;

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
        return Parboiled.createParser(Expressions.class, ConfigBuilder.defaultConfig().build());
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
