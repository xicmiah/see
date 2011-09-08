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
        assertThat(num("40"), is(node));

        node = parser.parse("40.45");
        assertThat(num("40.45"), is(node));

        assertThat(str("test string for test"), is(parser.parse("\"test string for test\"")));
    }

    @Test
    public void testParseOperators() throws Exception {
        assertThat(op("-", var("a"), var("b")), is(parser.parse("a - b")) );
        assertThat(op("+", var("a"), var("b")), is(parser.parse("a + b")) );
        assertThat(op("*", var("a"), var("b")), is(parser.parse("a * b")) );
        assertThat(op("/", var("a"), var("b")), is(parser.parse("a / b")) );
        assertThat(op("^", var("a"), var("b")), is(parser.parse("a^ b")) );
        assertThat(op("==", var("a"), var("b")), is(parser.parse("a == b")) );
        assertThat(op(">", var("a"), var("b")), is(parser.parse("a > b")) );
        assertThat(op("<", var("a"), var("b")), is(parser.parse("a < b")) );
        assertThat(op(">=", var("a"), var("b")), is(parser.parse("a >= b")) );
        assertThat(op("<=", var("a"), var("b")), is(parser.parse("a <= b")) );
        assertThat(op("&&", var("a"), var("b")), is(parser.parse("a && b")) );
        assertThat(op("||", var("a"), var("b")), is(parser.parse("a || b")) );

        assertThat(op("/", op("+", var("a"), var("c")), var("b")), is(parser.parse("(a + c)/ b")) );
        assertThat(op("+", var("a"), op("/", var("c"), var("b"))), is(parser.parse("a + c/ b")) );
    }

    @Test
    public void testFunctionCall() throws Exception {
        assertThat(fun("sum", var("a"), var("b")), is(parser.parse("sum(a,b)")));
        assertThat(fun("trace", var("a"), var("b"), num("5"), str("hello")), is(parser.parse("trace(a,b,5,\"hello\")")));
        assertThat(fun("sum", op("+", var("a"), var("c")), op("-", var("b"), var("a"))), is(parser.parse("sum(a + c,b - a)")));
    }

    public SimpleFunctionNode<Object, Object> fun(String functionName, SeeTreeNode<Object>... args){
        SimpleFunctionNode<Object, Object> func
                = new SimpleFunctionNode<Object, Object>(new ClassicToken(-1, functionName), gc.getFunctions().get(functionName));
        func.addChild(new DummyNode(new ClassicToken(-1, functionName)));
        func.addChild(new DummyNode(new ClassicToken(-1, "(")));
        func.addChild(new DummyNode(new ClassicToken(-1, ")")));
        for (SeeTreeNode<Object> arg : args){
            func.addChild(arg);
        }
        return func;
    }

    public OperatorNode<Object,Object> op(String opName, SeeTreeNode<Object>... args){
        OperatorNode<Object,Object> op = new OperatorNode<Object, Object>(new ClassicToken(-1, opName), gc.getFunctions().get(opName));
        for (SeeTreeNode<Object> arg: args){
            op.addChild(arg);
        }
        return op;
    }

    public ConstantTreeNode<Object> str(String value){
        return new ConstantTreeNode<Object>(new ClassicToken(-1,value), value);
    }

    public ConstantTreeNode<Object> num(String value){
        return new ConstantTreeNode<Object>(new ClassicToken(-1, value), gc.getNumberFactory().getNumber(value));
    }

    public VarTreeNode<Object> var(String value){
        ClassicToken token = new ClassicToken(-1, value);
        return new VarTreeNode<Object>(token);
    }
}
