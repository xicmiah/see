package see.exceptions;

/**
 * @author dpavlov
 */
public class TokenPosition {

    private final int startPosition;
    private final int endPosition;

    private final int lineNumber;
    private final int charPositionInLine;

    public TokenPosition(int startPosition, int endPosition) {
        this(startPosition, endPosition, -1, -1);
    }

    public TokenPosition(int startPosition, int endPosition, int lineNumber, int charPositionInLine) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lineNumber = lineNumber;
        this.charPositionInLine = charPositionInLine;
    }

    /**
     * Zero based index of token last character. This is the absolute shift from the string start
     *
     * @return absolute start index of the token
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * Zero based index of token last character. This is the absolute shift from the string start
     *
     * @return absolute end index of the token
     */
    public int getEndPosition() {
        return endPosition;
    }

    /**
     * Line number that starts with 1. So the first line will be 1, the second 2 etc.
     *
     * @return line number of token start
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Zero based index of first token char in line
     *
     * @return relative index in line where the token start
     */
    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    /**
     * Formatted string with this object data;
     *
     * @return formatted token position
     */
    public String getPositionDescription() {
        //todo write custom position formatting
        return toString();
    }

    @Override
    public String toString() {
        return "TokenPosition{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", lineNumber=" + lineNumber +
                ", charPositionInLine=" + charPositionInLine +
                '}';
    }
}
