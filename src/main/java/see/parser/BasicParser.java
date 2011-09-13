package see.parser;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.parboiled.Rule;
import org.parboiled.buffers.InputBuffer;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.exceptions.ParseErrorDescription;
import see.exceptions.ParseException;
import see.exceptions.TokenPosition;
import see.tree.Node;

import java.util.List;

public class BasicParser<T> implements Parser<T> {

    private final Rule rule;

    public BasicParser(Rule rule) {
        this.rule = rule;
    }

    @Override
    public Node<T> parse(String input) {
        ParseRunner<Node<T>> runner = new ReportingParseRunner<Node<T>>(rule);
        ParsingResult<Node<T>> result = runner.run(input);
        
        if (!result.matched) {
            throw new ParseException(extractErrors(result));
        }

        return result.resultValue;
    }

    private List<ParseErrorDescription> extractErrors(ParsingResult<?> result){
        return Lists.transform(result.parseErrors, new Function<ParseError, ParseErrorDescription>() {
            @Override
            public ParseErrorDescription apply(ParseError parseError) {
                InputBuffer buffer = parseError.getInputBuffer();
                String token  = buffer.extract(parseError.getStartIndex(), parseError.getEndIndex());

                return new ParseErrorDescription(
                        new TokenPosition(parseError.getStartIndex(), parseError.getEndIndex()),
                        token,
                        parseError.getErrorMessage()
                );
            }
        });
    }
}
