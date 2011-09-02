package see.parser;

import org.parboiled.Rule;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.exceptions.ParseException;
import see.tree.Node;

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
            throw new ParseException(result);
        }

        return result.resultValue;
    }
}
