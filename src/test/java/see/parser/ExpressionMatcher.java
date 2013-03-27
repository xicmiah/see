package see.parser;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.parboiled.Rule;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.parser.config.ConfigBuilder;
import see.parser.grammar.AltEntryPoints;

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

    @Override
    public void describeTo(Description description) {
        description.appendText(this.description);
    }

    private static AltEntryPoints getGrammar() {
        return AltEntryPoints.apply(ConfigBuilder.defaultConfig().build());
    }

    @Factory
    public static Matcher<String> singleExpression() {
        return new ExpressionMatcher(getGrammar().Simple().matcher(), "a single expression");
    }

    @Factory
    public static Matcher<String> returnExpression() {
        return new ExpressionMatcher(getGrammar().CalcExpression().matcher(), "an expression");
    }

    @Factory
    public static Matcher<String> expressionList() {
        return new ExpressionMatcher(getGrammar().Script().matcher(), "a list of expressions");
    }
}
