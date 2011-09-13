package see.exceptions;

/**
 * @author dpavlov
 */
public class FunctionNotFoundException extends SeeException {

    private final String functionName;

    private final TokenPosition tokenPosition;

    public FunctionNotFoundException(String functionName, TokenPosition tokenPosition) {
        this.functionName = functionName;
        this.tokenPosition = tokenPosition;
    }

    public String getFunctionName() {
        return functionName;
    }

    public TokenPosition getTokenPosition() {
        return tokenPosition;
    }

    @Override
    public String getMessage() {
        String position = tokenPosition != null ? tokenPosition.getPositionDescription() : "?";
        return String.format("Function '%s' not found at position %s", functionName, position);
    }

    @Override
    public String toString() {
        return "FunctionNotFoundException{" +
                "functionName='" + functionName + '\'' +
                ", functionPositionInInput=" + tokenPosition +
                '}';
    }
}
