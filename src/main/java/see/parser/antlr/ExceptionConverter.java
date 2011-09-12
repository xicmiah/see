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
            end = start = ((CommonToken) token).getStartIndex();

            String text = token.getText();
            if (!isEmpty(text)){
                end = start + text.length() - 1;
            }
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

        String message = lexer.getErrorHeader(e) + " " + lexer.getErrorMessage(e, lexer.getTokenNames());

        String tokenText = Character.toString((char) e.c);
        if (!isEmpty(token.getText())){
            tokenText = token.getText();
        }
        return new ParseErrorDescription(position, tokenText, message);
    }
}
