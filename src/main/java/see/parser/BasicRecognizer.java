package see.parser;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import see.parser.grammar.Expressions;
import see.tree.Node;

public class BasicRecognizer implements Parser<Number> {
    @Override
    public Node<Number> parse(String input) {
        Expressions parser = Parboiled.createParser(Expressions.class);

        ParseRunner<Node<Number>> runner = new ReportingParseRunner<Node<Number>>(parser.Condition());
        ParsingResult<Node<Number>> result = runner.run(input);

        return result.resultValue;
    }
}
