package see.parser.antlr;

import org.antlr.runtime.*;
import see.exceptions.ParseErrorDescription;
import see.exceptions.TokenPosition;

/**
 * @author dpavlov
 */
public class ExceptionConverter {

    public static TokenPosition extractTokenPosition(Token token) {

        int start = -1;
        int end = -1;
        if (token instanceof CommonToken) {
            CommonToken commonToken = (CommonToken) token;
            start = commonToken.getStartIndex();
            end = commonToken.getStopIndex();
        }
        int line = token.getLine();
        int positionInLine = token.getCharPositionInLine();

        return new TokenPosition(start, end, line, positionInLine);
    }

    private static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static ParseErrorDescription createLexerErrorDescription(RecognitionException e, Lexer lexer, RecognizerSharedState state) {
        CommonToken token = new CommonToken(lexer.getCharStream(), Token.INVALID_TOKEN_TYPE, Token.HIDDEN_CHANNEL, state.tokenStartCharIndex, lexer.getCharIndex());
        token.setCharPositionInLine(state.tokenStartCharPositionInLine);
        token.setLine(state.tokenStartLine);

        TokenPosition position = extractTokenPosition(token);

        String message = createErrorMessage(e, lexer);

        String tokenText = Character.toString((char) e.c);
        if (!isEmpty(token.getText())){
            tokenText = token.getText();
        }
        return new ParseErrorDescription(position, tokenText, message);
    }

    private static String createErrorMessage(RecognitionException e, BaseRecognizer baseRecognizer) {
        return baseRecognizer.getErrorHeader(e) + " " + baseRecognizer.getErrorMessage(e, baseRecognizer.getTokenNames());
    }

    public static ParseErrorDescription createParserErrorDescription(RecognitionException e, Parser parser, RecognizerSharedState state) {
        Token token = e.token;
        TokenPosition position = extractTokenPosition(token);
        String message = createErrorMessage(e, parser);
        String tokenText = null;
        if (token != null){
            tokenText = token.getText();
        }
        return new ParseErrorDescription(position, tokenText, message);
    }
}
