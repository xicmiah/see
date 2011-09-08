package see.parser.antlr;

import see.parser.config.GrammarConfiguration;

/**
 * @author pavlov
 * @since 08.09.11
 */
public interface AntlrParserFactory {

    AbstractAntlrGrammarParser getParser(GrammarConfiguration gc, String expression);

}
