package see.integration;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import see.See;
import see.tree.Node;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class TreeEquivalenceTest {
    See see = new See();

    @DataPoints
    public static final String[] data = {"a", "1+2", "sum(2,3)", "9+42+100500.0", "a = 5", "isDefined(crn)"};

    @Theory
    public void testEquivalence(String example) throws Exception {
        Node<Object> run1 = see.parseExpression(example);
        Node<Object> run2 = see.parseExpression(example);

        assertEquals(run1, run2);
    }
}
