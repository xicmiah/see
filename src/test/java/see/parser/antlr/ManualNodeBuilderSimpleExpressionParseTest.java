package see.parser.antlr;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.tree.Node;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static see.parser.antlr.ImmutableNodesBuilder.*;

/**
 * @author pavlov
 * @since 09.09.11
 */
public class ManualNodeBuilderSimpleExpressionParseTest {

    private static final String UNREGISTERED_FUNCITON_NAME = "someUnregisteredFunction";
    private ManualTreeBuildParser<Object> parser;
    private static GrammarConfiguration gc;

    @BeforeClass
    public static void setUpClass() {
        gc = ConfigBuilder.defaultConfig().build();
    }

    @Before
    public void setUp() throws Exception {
        parser = new ManualTreeBuildParser<Object>(gc, false);
    }

    @Test
    public void testParseVariable() throws Exception {
        assertThat(parser.parse("a"), is(var("a")));
    }

    @Test
    public void testParseConstant() throws Exception {
        Node<Object> node = parser.parse("40");
        assertThat(node, is(num(gc, "40")));

        node = parser.parse("40.45");
        assertThat(node, is(num(gc, "40.45")));

        assertThat(parser.parse("\"test string for test\""), is(str("test string for test")));
    }

    @Test
    public void testParseOperators() throws Exception {
        assertThat(parser.parse("a - b"), is(op(gc, "-", var("a"), var("b"))));
        assertThat(parser.parse("a + b"), is(op(gc, "+", var("a"), var("b"))));
        assertThat(parser.parse("a * b"), is(op(gc, "*", var("a"), var("b"))));
        assertThat(parser.parse("a / b"), is(op(gc, "/", var("a"), var("b"))));
        assertThat(parser.parse("a^ b"), is(op(gc, "^", var("a"), var("b"))));
        assertThat(parser.parse("a == b"), is(op(gc, "==", var("a"), var("b"))));
        assertThat(parser.parse("a > b"), is(op(gc, ">", var("a"), var("b"))));
        assertThat(parser.parse("a < b"), is(op(gc, "<", var("a"), var("b"))));
        assertThat(parser.parse("a >= b"), is(op(gc, ">=", var("a"), var("b"))));
        assertThat(parser.parse("a <= b"), is(op(gc, "<=", var("a"), var("b"))));
        assertThat(parser.parse("a && b"), is(op(gc, "&&", var("a"), var("b"))));
        assertThat(parser.parse("a || b"), is(op(gc, "||", var("a"), var("b"))));


        assertThat(parser.parse("(a + c)/ b"), is(op(gc, "/", op(gc, "+", var("a"), var("c")), var("b"))));
        assertThat(parser.parse("a + c/ b"), is(op(gc, "+", var("a"), op(gc, "/", var("c"), var("b")))));
    }

    @Test
    public void testSameOperationsList() throws Exception {
        assertThat(parser.parse("a+b+c+d"), is(
                op(gc, "+",
                        op(gc, "+",
                                op(gc, "+", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));

        assertThat(parser.parse("a-b-c-d"), is(
                op(gc, "-",
                        op(gc, "-",
                                op(gc, "-", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));

        assertThat(parser.parse("a*b*c*d"), is(
                op(gc, "*",
                        op(gc, "*",
                                op(gc, "*", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));

        assertThat(parser.parse("a/b/c/d"), is(
                op(gc, "/",
                        op(gc, "/",
                                op(gc, "/", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));
    }

    @Test
    public void testSameLogicalOperationList() throws Exception {
        assertThat(parser.parse("a>b>c>d"), is(
                op(gc, ">",
                        op(gc, ">",
                                op(gc, ">", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));

        assertThat(parser.parse("a<b<c<d"), is(
                op(gc, "<",
                        op(gc, "<",
                                op(gc, "<", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));

        assertThat(parser.parse("a==b==c==d"), is(
                op(gc, "==",
                        op(gc, "==",
                                op(gc, "==", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));

        assertThat(parser.parse("a!=b!=c!=d"), is(
                op(gc, "!=",
                        op(gc, "!=",
                                op(gc, "!=", var("a"), var("b")),
                                var("c")
                        ),
                        var("d")
                )
        ));
    }

    @Test
    public void testFunctionCall() throws Exception {
        assertThat(parser.parse("sum(a,b)"), is(fun(gc, "sum", var("a"), var("b"))));
        assertThat(parser.parse("sum(a,b,5,\"hello\")"), is(fun(gc, "sum", var("a"), var("b"), num(gc, "5"), str("hello"))));
        assertThat(parser.parse("sum(a + c,b - a)"), is(fun(gc, "sum", op(gc, "+", var("a"), var("c")), op(gc, "-", var("b"), var("a")))));
    }


    @Test(expected = RuntimeException.class)
    public void testNotRegisteredFunctionCall() throws Exception {
        assertNull(gc.getFunctions().get(UNREGISTERED_FUNCITON_NAME));
        parser.parse(UNREGISTERED_FUNCITON_NAME + "()");
    }

}
