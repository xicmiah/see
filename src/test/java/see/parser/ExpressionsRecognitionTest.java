package see.parser;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import see.parser.grammar.Expressions;

import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class ExpressionsRecognitionTest {

    @DataPoints
    public static final String[] logical = {"100500 || c != 9 && a==42"};
    
    @DataPoints
    public static final String[] equality = {"c==9", "a != 42"};

    @DataPoints
    public static final String[] relational = {"1<2", "2 <=3", "9>= 42", "42 > 100500"};

    @DataPoints
    public static final String[] additive = {"1+2", "1+2+3", "1 +2 + 3"};

    @DataPoints
    public static final String[] multiplicative = {"1*2", "1*2*3", "1*2 + 2* 3 + 4"};

    @DataPoints
    public static final String[] unary = {"!9", "-9", "-a", "+9", "+a"};

    @DataPoints
    public static final String[] power = {"9^42", "100500^-9", "(100500)^(9-42)"};

    @DataPoints
    public static final String[] parens = {"1*(2+3)", "(9 + 42)", "1 + (2 + (3+4)) "};

    @DataPoints
    public static final String[] floats = {"0.9", "9.0e6", "42.", ".9", "42.e9"};

    @DataPoints
    public static final String[] functions = {"sum(9, 42)", "cos(0)", "win()"};


    /**
     * Test that grammar recognizes input as valid simple expression (Condition() entry point)
     * @param example input to test
     * @throws Exception
     */
    @Theory
    public void testRecognition(String example) throws Exception {
        assertThat(example, ConditionMatcher.condition());
    }

    /**
     * Matcher for expressions
     */
    public static class ConditionMatcher extends TypeSafeMatcher<String> {
        private final Expressions grammar = Parboiled.createParser(Expressions.class);
        private final ParseRunner<?> runner  = new ReportingParseRunner<Object>(grammar.Condition());

        @Override
        public boolean matchesSafely(String item) {
            return runner.run(item).matched;
        }

        public void describeTo(Description description) {
            description.appendText("a condition");
        }

        @Factory
        public static Matcher<String> condition() {
            return new ConditionMatcher();
        }
    }

}
