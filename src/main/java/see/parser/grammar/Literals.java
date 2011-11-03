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
                Ch(delimiter)
        );
    }

    Rule IntLiteral() {
        return OneOrMore(Digit());
    }

    Rule FloatLiteral(){
        return FirstOf(
                Sequence(OneOrMore(Digit()), decimalSeparator, OneOrMore(Digit()), Optional(Exponent())),
                Sequence(decimalSeparator, OneOrMore(Digit()), Optional(Exponent())),
                Sequence(OneOrMore(Digit()), Exponent())
        );
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

    Rule BooleanLiteral() {
        return FirstOf(String("true"),String("false"));
    }

    Rule Digit() {
        return CharRange('0', '9');
    }
}
