package see.parser.antlr;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.tree.Node;

import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;
import static see.parser.antlr.ImmutableNodesBuilder.*;

/**
 * @author pavlov
 * @since 09.09.11
 */
public class ManualNodeBuilderSequentialExpressionParseTest {

    private ManualTreeBuildParser<Object> parser;
    private static GrammarConfiguration gc;

    @BeforeClass
    public static void setUpClass() {
        gc = ConfigBuilder.defaultConfig().build();
    }

    @Before
    public void setUp() throws Exception {
        parser = new ManualTreeBuildParser<Object>(gc, true);
    }

    @Test
    public void testReturnOnlyExpression() throws Exception {
        assumeThat(parser.parse("return 1;"), is(num(gc, "1")));
        assumeThat(parser.parse("return a;"), is(var("a")));
        assumeThat(parser.parse("return \"1\";"), is(str("1")));
        assumeThat(parser.parse("return sum();"), is(fun(gc, "sum")));
        assumeThat(parser.parse("return a+b;"), is(op(gc, "+", var("a"), var("b"))));
    }

    @Test
    public void testSimpleExpressionsWithoutReturn() throws Exception {
        assumeThat(parser.parse("sum(); a+b;"), is(seq(gc, fun(gc, "sum"), op(gc, "+", var("a"), var("b")))));
        assumeThat(parser.parse("sum(); a;"), is(seq(gc, fun(gc, "sum"), var("a"))));
        assumeThat(parser.parse("sum(); \"hello\";"), is(seq(gc, fun(gc, "sum"), str("hello"))));
        assumeThat(parser.parse("sum(); 2.5;"), is(seq(gc, fun(gc, "sum"), num(gc, "2.5"))));
        assumeThat(parser.parse("sum(); sum(a,b);"), is(seq(gc, fun(gc, "sum"), fun(gc, "sum", var("a"), var("b")))));
    }

    @Test
    public void testListOfSimpleExpressionsWithReturn() throws Exception {
        assumeThat(parser.parse("sum(); return a+b;"), is(seq(gc, fun(gc, "sum"), op(gc, "+", var("a"), var("b")))));
        assumeThat(parser.parse("sum(); return a;"), is(seq(gc, fun(gc, "sum"), var("a"))));
        assumeThat(parser.parse("sum(); return \"hello\";"), is(seq(gc, fun(gc, "sum"), str("hello"))));
        assumeThat(parser.parse("sum(); return 2.5;"), is(seq(gc, fun(gc, "sum"), num(gc, "2.5"))));
        assumeThat(parser.parse("sum(); return sum(a,b);"), is(seq(gc, fun(gc, "sum"), fun(gc, "sum", var("a"), var("b")))));
    }

    @Test
    public void testIfStatement() throws Exception {
        Node<Object> ifNode = op(gc, "if", var("a"), seq(gc, var("b")), seq(gc, var("c")));
        assumeThat(parser.parse("if (a) then {b;} else {c;}"), is(ifNode));
    }

    @Test
    public void testEmptyIfStatement() throws Exception {
        Node<Object> emptyIf = op(gc, "if", num(gc, "1.009"), seq(gc), seq(gc));
        assumeThat(parser.parse("if (1.009) then {} else {}"), is(emptyIf));
    }

    @Test
    public void testIfNodeWithoutElse() throws Exception {
        Node<Object> ifNodeWithoutElse = op(gc, "if",
                num(gc, "1"),
                seq(gc,
                        fun(gc, "=",
                                var("a"),
                                fun(gc, "sum",
                                        num(gc, "1"),
                                        var("var")
                                )
                        ),
                        var("a")
                ),
                seq(gc)
        );
        assumeThat(parser.parse("if (1) then {a = sum(1,var); a;}"), is(ifNodeWithoutElse));
    }

}
