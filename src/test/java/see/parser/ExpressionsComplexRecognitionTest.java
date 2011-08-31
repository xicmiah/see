package see.parser;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;
import static see.parser.ExpressionMatcher.expressionList;
import static see.parser.ExpressionMatcher.returnExpression;

@RunWith(Theories.class)
public class ExpressionsComplexRecognitionTest {
    @DataPoints
    public static final String[] returns = {"", "c || r; n <= 9; i && n != 42"};

    @DataPoints
    public static final String[] assigns = {"a = 4", "a = b = 42"};

    @DataPoints
    public static final String[] conditions = {"if (c != 9) then {1} else {2}", "if (c != 9) then {1}"};

    /**
     * Test that all non-empty inputs are valid expression lists
     * @param expression
     * @throws Exception
     */
    @Theory
    public void testExpressionRecognition(String expression) throws Exception {
        assumeTrue(!expression.isEmpty());
        assertThat(expression, expressionList());
    }

    @Theory
    public void testAdditionalSemicolons(String expression) throws Exception {
        assumeThat(expression, expressionList());
        assertThat(expression + ";", expressionList());
    }

    @Theory
    public void testAddingReturn(String expression) throws Exception {
        assumeThat(expression, expressionList());
        assertThat(expression + " return 9", returnExpression());
    }
}
