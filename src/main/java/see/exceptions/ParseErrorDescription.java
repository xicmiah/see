package see.exceptions;

/**
 * @author dpavlov
 */
public class ParseErrorDescription {

    private final TokenPosition tokenPosition;

    private final String token;

    private final String message;

    public ParseErrorDescription(TokenPosition tokenPosition, String token, String message) {
        this.tokenPosition = tokenPosition;
        this.token = token;
        this.message = message;
    }

    public TokenPosition getTokenPosition() {
        return tokenPosition;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ParseErrorDescription{" +
                "tokenPosition=" + tokenPosition +
                ", token='" + token + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
