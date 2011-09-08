package see.parser.antlr;

import org.antlr.runtime.ClassicToken;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.parser.antlr.tree.*;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.parser.config.LocalizedBigDecimalFactory;
import see.tree.Node;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import static see.parser.antlr.TreeBuilderHelper.*;
/**
 * @author pavlov
 * @since 06.09.11
 */
public class SimpleAntlrExpressionParseTest {

    private AntlrExpressionParser<Object> parser;
    private static GrammarConfiguration gc;

    @BeforeClass
    public static void setUpClass() {
        gc = ConfigBuilder.defaultConfig().build();
    }

    @Before
    public void setUp() throws Exception {
        parser = new AntlrExpressionParser<Object>(gc);
        parser.setParseMultipleExpressions(false);
        parser.setParserFactory(new AntlrParserFactoryImpl());
    }

    @Test
    public void testParseVariable() throws Exception {
        Node<Object> stringNode = parser.parse("a");
        assertThat(var("a"), is(stringNode));
    }

    @Test
    public void testParseConstant() throws Exception {
        Node<Object> node = parser.parse("40");
        assertThat(num(gc, "40"), is(node));

        node = parser.parse("40.45");
        assertThat(num(gc, "40.45"), is(node));

        assertThat(str("test string for test"), is(parser.parse("\"test string for test\"")));
    }

    @Test
    public void testParseOperators() throws Exception {
        assertThat(op(gc, "-", var("a"), var("b")), is(parser.parse("a - b")) );
        assertThat(op(gc, "+", var("a"), var("b")), is(parser.parse("a + b")) );
        assertThat(op(gc, "*", var("a"), var("b")), is(parser.parse("a * b")) );
        assertThat(op(gc, "/", var("a"), var("b")), is(parser.parse("a / b")) );
        assertThat(op(gc, "^", var("a"), var("b")), is(parser.parse("a^ b")) );
        assertThat(op(gc, "==", var("a"), var("b")), is(parser.parse("a == b")) );
        assertThat(op(gc, ">", var("a"), var("b")), is(parser.parse("a > b")) );
        assertThat(op(gc, "<", var("a"), var("b")), is(parser.parse("a < b")) );
        assertThat(op(gc, ">=", var("a"), var("b")), is(parser.parse("a >= b")) );
        assertThat(op(gc, "<=", var("a"), var("b")), is(parser.parse("a <= b")) );
        assertThat(op(gc, "&&", var("a"), var("b")), is(parser.parse("a && b")) );
        assertThat(op(gc, "||", var("a"), var("b")), is(parser.parse("a || b")) );

        assertThat(op(gc, "/", op(gc, "+", var("a"), var("c")), var("b")), is(parser.parse("(a + c)/ b")) );
        assertThat(op(gc, "+", var("a"), op(gc, "/", var("c"), var("b"))), is(parser.parse("a + c/ b")) );
    }

    @Test
    public void testFunctionCall() throws Exception {
        assertThat(fun(gc, "sum", var("a"), var("b")), is(parser.parse("sum(a,b)")));
        assertThat(fun(gc, "sum", var("a"), var("b"), num(gc, "5"), str("hello")), is(parser.parse("sum(a,b,5,\"hello\")")));
        assertThat(fun(gc, "sum", op(gc, "+", var("a"), var("c")), op(gc, "-", var("b"), var("a"))), is(parser.parse("sum(a + c,b - a)")));
    }


}
