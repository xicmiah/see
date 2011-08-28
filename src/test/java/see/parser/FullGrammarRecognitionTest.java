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
import see.tree.Node;

import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class FullGrammarRecognitionTest {
    FullGrammar fullGrammar = Parboiled.createParser(FullGrammar.class);
    ParseRunner<Node<Number>> runner = new ReportingParseRunner<Node<Number>>(fullGrammar.Condition());

    @DataPoints
    public static final String[] additive = {"1+2", "1+2+3", "1 +2 + 3"};

    @DataPoints
    public static final String[] multiplicative = {"1*2", "1*2*3", "1*2 + 2* 3 + 4"};


    @Theory
    public void testRecognition(String example) throws Exception {
        assertThat(example, ConditionMatcher.condition());
    }

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
