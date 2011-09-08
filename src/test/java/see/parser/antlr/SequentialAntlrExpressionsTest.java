package see.parser.antlr;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.tree.Node;

import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;
import static see.parser.antlr.TreeBuilderHelper.*;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class SequentialAntlrExpressionsTest {

    private AntlrExpressionParser<Object> parser;
    private static GrammarConfiguration gc;

    @BeforeClass
    public static void setUpClass() {
        gc = ConfigBuilder.defaultConfig().build();
    }

    @Before
    public void setUp() throws Exception {
        parser = new AntlrExpressionParser<Object>(gc);
        parser.setParseMultipleExpressions(true);
        parser.setParserFactory(new AntlrParserFactoryImpl());
    }

    @Test
    public void testReturnOnlyExpression() throws Exception {
        assumeThat(tr(num(gc, "1")), is(parser.parse("return 1;")));
        assumeThat(tr(var("a")), is(parser.parse("return a;")));
        assumeThat(tr(str("1")), is(parser.parse("return \"1\";")));
        assumeThat(tr(fun(gc, "sum")), is(parser.parse("return sum();")));
        assumeThat(tr(op(gc, "+", var("a"), var("b"))), is(parser.parse("return a+b;")));
    }

    @Test
    public void testSimpleExpressionsWithoutReturn() throws Exception {
        assumeThat(seq(gc, fun(gc, "sum"), op(gc, "+", var("a"), var("b"))), is(parser.parse("sum(); a+b;")));
        assumeThat(seq(gc, fun(gc, "sum"), var("a")), is(parser.parse("sum(); a;")));
        assumeThat(seq(gc, fun(gc, "sum"), str("hello")), is(parser.parse("sum(); \"hello\";")));
        assumeThat(seq(gc, fun(gc, "sum"), num(gc, "2.5")), is(parser.parse("sum(); 2.5;")));
        assumeThat(seq(gc, fun(gc, "sum"), fun(gc, "sum", var("a"), var("b"))), is(parser.parse("sum(); sum(a,b);")));
    }

    @Test
    public void testListOfSimpleExpressionsWithReturn() throws Exception {
        assumeThat(seq(gc, fun(gc, "sum"), tr(op(gc, "+", var("a"), var("b")))), is(parser.parse("sum(); return a+b;")));
        assumeThat(seq(gc, fun(gc, "sum"), tr(var("a"))), is(parser.parse("sum(); return a;")));
        assumeThat(seq(gc, fun(gc, "sum"), tr(str("hello"))), is(parser.parse("sum(); return \"hello\";")));
        assumeThat(seq(gc, fun(gc, "sum"), tr(num(gc, "2.5"))), is(parser.parse("sum(); return 2.5;")));
        assumeThat(seq(gc, fun(gc, "sum"), tr(fun(gc, "sum", var("a"), var("b")))), is(parser.parse("sum(); return sum(a,b);")));
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
                )
        );
        assumeThat(parser.parse("if (1) then {a = sum(1,var); a;}"), is(ifNodeWithoutElse));
    }
}
