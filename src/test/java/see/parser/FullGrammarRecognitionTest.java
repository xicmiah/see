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

import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class FullGrammarRecognitionTest {

    @DataPoints
    public static final String[] additive = {"1+2", "1+2+3", "1 +2 + 3"};

    @DataPoints
    public static final String[] multiplicative = {"1*2", "1*2*3", "1*2 + 2* 3 + 4"};

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
        private final FullGrammar grammar = Parboiled.createParser(FullGrammar.class);
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
