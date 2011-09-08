package see.parser.antlr;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.parser.antlr.tree.SeeTreeAdaptor;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.parser.config.LocalizedBigDecimalFactory;

import java.util.Locale;

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
    public void testName() throws Exception {

    }
}
