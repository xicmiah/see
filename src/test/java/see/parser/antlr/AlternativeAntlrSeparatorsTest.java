package see.parser.antlr;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.parser.config.LocalizedBigDecimalFactory;
import see.tree.Node;
import see.tree.immutable.ImmutableConstNode;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static see.parser.antlr.ImmutableNodesBuilder.fun;
import static see.parser.antlr.ImmutableNodesBuilder.var;

/**
 * @author pavlov
 * @since 09.09.11
 */
public class AlternativeAntlrSeparatorsTest {

    private ManualTreeBuildParser<Object> parser;
    private static GrammarConfiguration gc;

    @BeforeClass
    public static void setUpClass() {
        Locale altSeparatorsLocale = new Locale("ru", "RU");
        gc = ConfigBuilder.defaultConfig()
                .setNumberFactory(new LocalizedBigDecimalFactory(altSeparatorsLocale))
                .build();
    }

    @Before
    public void setUp() throws Exception {
        parser = new ManualTreeBuildParser<Object>(gc, false);
    }

    @Test
    public void testTestAlternativeSeparatorInNumberFactory() throws Exception {
        Assert.assertEquals(new Character(','), gc.getNumberFactory().getDecimalSeparator());
    }

    @Test
    public void testNumberParse() throws Exception {
        Node<Object> constNode = new ImmutableConstNode(new BigDecimal("0.40"));
        Assert.assertThat(parser.parse("0,40"), is(constNode));
    }

    @Test
    public void testFunctionArgumentsParse() throws Exception {
        Assert.assertThat(parser.parse("sum(a;b;c)"), is(fun(gc, "sum", var("a"), var("b"), var("c"))));
    }
}
