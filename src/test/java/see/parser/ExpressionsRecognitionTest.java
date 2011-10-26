package see.parser;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static see.parser.ExpressionMatcher.returnExpression;
import static see.parser.ExpressionMatcher.singleExpression;

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
    public static final String[] floats = {"0.9", "9.0e6", ".9", "42.e9"};

    @DataPoints
    public static final String[] strings = {"\"\"", "\"c\"", "\"bk\""};

    @DataPoints
    public static final String[] functions = {"sum(9, 42)", "sum(0)", "sum()"};

    @DataPoints
    public static final String[] assigns = {"a = 4", "a = b = 42"};

    /**
     * Test that grammar recognizes input as valid simple expression (Condition() entry point)
     * @param example input to test
     * @throws Exception
     */
    @Theory
    public void testRecognition(String example) throws Exception {
        assertThat(example, singleExpression());
    }

    /**
     * Test that appending "return 9" to expression matches CalcExpression() rule.
     * @param example input to test
     */
    @Theory
    public void testAppendingReturn(String example) {
        assumeThat(example, singleExpression());
        assertThat(example + ";return 9", returnExpression());
    }
}
