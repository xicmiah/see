package see.exceptions;

/**
 * @author dpavlov
 */
public class ParseErrorDescription {

    private final int startPosition;
    private final int endPosition;

    private final int lineNumber;
    private final int charPositionInLine;

    private final String token;

    private final String message;

    public ParseErrorDescription(int startPosition, int endPosition, String token, String message) {
        this(startPosition, endPosition, token, message, -1, -1);
    }

    public ParseErrorDescription(int startPosition, int endPosition, String token, String message, int lineNumber, int charPositionInLine) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.token = token;
        this.message = message;
        this.lineNumber = lineNumber;
        this.charPositionInLine = charPositionInLine;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ParseError{" +
                "from=" + startPosition +
                ", to=" + endPosition +
                ", inLine=" + lineNumber +
                ", positionInLine=" + charPositionInLine +
                ", token='" + token + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
