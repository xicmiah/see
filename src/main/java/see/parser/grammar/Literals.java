package see.parser.grammar;

import org.parboiled.Rule;

class Literals extends AbstractGrammar {
    final Character decimalSeparator;

    public Literals(Character decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    Rule StringLiteral() {
        return FirstOf(DelimitedString('"'), DelimitedString('\''));
    }

    Rule DelimitedString(char delimiter) {
        return Sequence(
                Ch(delimiter),
                ZeroOrMore(Sequence(TestNot(delimiter), ANY)).suppressSubnodes(),
                Ch(delimiter),
                Whitespace()
        );
    }

    Rule IntLiteral() {
        return OneOrMore(Digit(), Whitespace());
    }

    Rule FloatLiteral(){
        return Sequence(FirstOf(
                Sequence(OneOrMore(Digit()), decimalSeparator, OneOrMore(Digit()), Optional(Exponent())),
                Sequence(decimalSeparator, OneOrMore(Digit()), Optional(Exponent())),
                Sequence(OneOrMore(Digit()), Exponent())
        ), Whitespace());
    }


    Rule Exponent() {
        return Sequence(AnyOf("eE"), Optional(AnyOf("+-")), OneOrMore(Digit()));
    }

    Rule Letter() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_');
    }

    Rule LetterOrDigit() {
        return FirstOf(Letter(), Digit());
    }

    Rule Digit() {
        return CharRange('0', '9');
    }
}
