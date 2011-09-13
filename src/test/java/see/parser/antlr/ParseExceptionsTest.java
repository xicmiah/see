package see.parser.antlr;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import see.exceptions.FunctionNotFoundException;
import see.exceptions.ParseErrorDescription;
import see.exceptions.ParseException;
import see.exceptions.TokenPosition;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author dpavlov
 */
public class ParseExceptionsTest {

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
    public void testLexerWrongSymbols() throws Exception {
        testLexerParseException("#", 0,0);
        testLexerParseException("sin#()", 3,3);
        testLexerParseException("a + @", 4,4);
        testLexerParseException("a \n+ @", 5,5,2,2);
    }

    @Test
    public void testLexerOnlyFirstWrongSymbolGathered() throws Exception {
        testLexerParseException("sin#$~#()", 3,3);
    }

    private void testLexerParseException(String input, int start, int end){
        testLexerParseException(input, start, end, 1, start);
    }

    private void testLexerParseException(String input, int start, int end, int line, int charInLine){
        try{
            parser.parse(input);
        }catch (ParseException pe){
            ParseErrorDescription ped = pe.getFirstError();
            assertNotNull("Parser error description should not be null", ped);
            TokenPosition tp = ped.getTokenPosition();
            assertEquals("wrong start postion", start, tp.getStartPosition());
            assertEquals("wrong end position", end, tp.getEndPosition());
            assertEquals("wrong line number", line, tp.getLineNumber());
            assertEquals("wrong charIndex in line", charInLine, tp.getCharPositionInLine());
        }
    }

    @Test
    public void testParserParseException() throws Exception {
        testParserParseException("sum() + ", 8,8);
        testParserParseException("1 + )", 4,4);
        testParserParseException("1 + 2a3cde3", 5,10);
        testParserParseException("a + (1-3!3)", 8,8);
    }

    private void testParserParseException(String input, int start, int end){
        testParserParseException(input, start, end, 1, start);
    }

    private void testParserParseException(String input, int start, int end, int line, int charInLine){
        try{
            parser.parse(input);
        }catch (ParseException ex){
            ParseErrorDescription ped = ex.getFirstError();
            assertNotNull("Parsing error description should not be null", ped);
            TokenPosition tp = ped.getTokenPosition();
            assertEquals("wrong start postion", start, tp.getStartPosition());
            assertEquals("wrong end position", end, tp.getEndPosition());
            assertEquals("wrong line number", line, tp.getLineNumber());
            assertEquals("wrong charIndex in line", charInLine, tp.getCharPositionInLine());
        }
    }

    @Test(expected = FunctionNotFoundException.class)
    public void testUnknownFunctionException() throws Exception {
        parser.parse("someUnknownFunction()");
    }

    @Test
    public void testTokenPositionForNotFoundFunction() throws Exception {
        String unknownFunctionName = "someUnknownFunction";
        try{
            parser.parse("1 + 3*" + unknownFunctionName + "()");
        }catch (FunctionNotFoundException fnf){
            TokenPosition position = fnf.getTokenPosition();
            Assert.assertNotNull(position);

            assertThat(position.getLineNumber(), is(1));
            assertThat(position.getCharPositionInLine(), is(6));
            assertThat(position.getStartPosition(), is(6));
            assertThat(position.getEndPosition(), is(6 + unknownFunctionName.length()-1));
        }
    }
}
