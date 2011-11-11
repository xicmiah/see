package see.parser.grammar;

import org.parboiled.Rule;

class Literals extends AbstractGrammar {
    final Character decimalSeparator;

    public Literals(Character decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    @WhitespaceSafe
    Rule StringLiteral() {
        return FirstOf(DelimitedString('"'), DelimitedString('\''));
    }

    @WhitespaceSafe
    Rule DelimitedString(char delimiter) {
        return Sequence(
                Ch(delimiter),
                ZeroOrMore(Sequence(TestNot(delimiter), ANY)).suppressSubnodes(),
                Ch(delimiter)
        );
    }

    @WhitespaceSafe
    Rule IntLiteral() {
        return OneOrMore(Digit());
    }

    @WhitespaceSafe
    Rule FloatLiteral(){
        return FirstOf(
                Sequence(OneOrMore(Digit()), decimalSeparator, OneOrMore(Digit()), Optional(Exponent())),
                Sequence(decimalSeparator, OneOrMore(Digit()), Optional(Exponent())),
                Sequence(OneOrMore(Digit()), Exponent())
        );
    }

    @WhitespaceSafe
    Rule Exponent() {
        return Sequence(AnyOf("eE"), Optional(AnyOf("+-")), OneOrMore(Digit()));
    }

    @WhitespaceSafe
    Rule Letter() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_', '$');
    }

    @WhitespaceSafe
    Rule LetterOrDigit() {
        return FirstOf(Letter(), Digit());
    }

    @WhitespaceSafe
    Rule BooleanLiteral() {
        return FirstOf(String("true"),String("false"));
    }

    @WhitespaceSafe
    Rule Digit() {
        return CharRange('0', '9');
    }
}
