package see.parser;

import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.parser.grammar.Expressions;
import see.tree.Node;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExpressionsTreeTest {
    private Expressions grammar = Parboiled.createParser(Expressions.class);
    private ParseRunner<Node<Object>> runner = new ReportingParseRunner<Node<Object>>(grammar.Condition());

    @Test
    public void testParsing() throws Exception {
        ParsingResult<Node<Object>> result = runner.run("sum(cos(c), 9)");
        assertTrue(result.matched);
        assertNotNull(result.resultValue);
    }
}
