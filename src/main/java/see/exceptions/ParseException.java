package see.exceptions;

import org.parboiled.errors.ParseError;
import org.parboiled.support.ParsingResult;

import java.util.Collections;
import java.util.List;

/**
 * An exception which happened during expression parsing
 */
public class ParseException extends SeeException {
    private final List<ParseErrorDescription> errors;

    public ParseException(ParseErrorDescription error) {
        errors = Collections.singletonList(error);
    }

    public ParseException(ParseErrorDescription error, Throwable cause) {
        super(cause);
        this.errors = Collections.singletonList(error);
    }

    public ParseException(List<ParseErrorDescription> errors) {
        this.errors = errors;
    }

    public ParseException(List<ParseErrorDescription> errors, Throwable cause) {
        super(cause);
        this.errors = errors;
    }

    public ParseErrorDescription getFirstError(){
        if (errors != null && errors.size() > 0){
            return errors.get(0);
        }else{
            return null;
        }
    }

    public List<ParseErrorDescription> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ParseException");
        sb.append("{").append(errors).append('}');
        return sb.toString();
    }
}
