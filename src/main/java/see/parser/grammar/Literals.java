package see.parser.grammar;

import org.parboiled.Rule;

public class Literals extends AbstractGrammar {
    Rule StringLiteral() {
        return Sequence(Ch('"'), ZeroOrMore(Sequence(TestNot("\""), ANY)).suppressSubnodes(), Ch('"'), Whitespace());
    }

    Rule IntLiteral() {
        return OneOrMore(Digit(), Whitespace());
    }


    Rule FloatLiteral(){
        return Sequence(FirstOf(
                Sequence(OneOrMore(Digit()), DecimalSeparator(), ZeroOrMore(Digit()), Optional(Exponent())),
                Sequence(DecimalSeparator(), OneOrMore(Digit()), Optional(Exponent())),
                Sequence(OneOrMore(Digit()), Exponent())
        ), Whitespace());
    }

    Rule DecimalSeparator() {
        return Ch('.');
    }

    Rule Exponent() {
        return Sequence(AnyOf("eE"), Optional(AnyOf("+-")), OneOrMore(Digit()));
    }

    Rule Letter() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '-');
    }

    Rule LetterOrDigit() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '-', Digit());
    }

    Rule Digit() {
        return CharRange('0', '9');
    }
}
