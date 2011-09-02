package see.exceptions;

import org.parboiled.errors.ParseError;
import org.parboiled.support.ParsingResult;

import java.util.List;

/**
 * An exception which happened during expression parsing
 */
public class ParseException extends SeeException {
    private final List<ParseError> errors;

    public ParseException(ParsingResult<?> result) {
        errors = result.parseErrors;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ParseException");
        sb.append("{").append(errors).append('}');
        return sb.toString();
    }
}
