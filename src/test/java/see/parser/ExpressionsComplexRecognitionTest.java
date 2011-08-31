package see.parser;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static see.parser.ConditionMatcher.returnExpression;

@RunWith(Theories.class)
public class ExpressionsComplexRecognitionTest {

    @DataPoints
    public static final String[] returns = {"return 9", "a = 5 return a", "a=5; b return 9", "a=5; b; return 9"};

    @DataPoints
    public static final String[] assigns = {"a = 4 return 9", "a = b = 42; return 9"};

    @DataPoints
    public static final String[] conditions = {"if (c != 9) then {1} else {2} return 9", "if (c != 9) then {1} return 9"};

    @Theory
    public void testComplexRecognition(String expression) throws Exception {
        assertThat(expression, returnExpression());
    }
}
