package see.parser;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import see.tree.Node;

public class BasicRecognizer implements Parser<Number> {
    @Override
    public Node<Number> parse(String input) {
        FullGrammar parser = Parboiled.createParser(FullGrammar.class);

        ParseRunner<Node<Number>> runner = new ReportingParseRunner<Node<Number>>(parser.Expression());
        ParsingResult<Node<Number>> result = runner.run(input);
        System.out.println(ParseTreeUtils.printNodeTree(result));

        return result.resultValue;
    }
}
