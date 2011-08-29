package see.parser;

import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class FullGrammar extends MacroGrammar {
    @Override
    Rule RightExpression() {
        return OrExpression();
    }

    Rule OrExpression() {
        return rep1sep(AndExpression(), "||");
    }

    Rule AndExpression() {
        return rep1sep(EqualExpression(), "&&");
    }

    Rule EqualExpression() {
        return rep1sep(RelationalExpression(), FirstOf("!=", "=="));
    }

    Rule RelationalExpression() {
        return rep1sep(AdditiveExpression(), FirstOf("<", ">", "<=", ">="));
    }

    Rule AdditiveExpression() {
        return rep1sep(MultiplicativeExpression(), FirstOf("+", "-"));
    }

    Rule MultiplicativeExpression() {
        return rep1sep(UnaryExpression(), FirstOf("*", "/"));
    }

    Rule UnaryExpression() {
        return FirstOf(Sequence(AnyOf("+-!"), UnaryExpression()), PowerExpression());
    }

    Rule PowerExpression() {
        return Sequence(UnaryExpressionNotPlusMinus(), Optional("^", UnaryExpression()));
    }

    Rule UnaryExpressionNotPlusMinus() {
        return FirstOf(Constant(), Function(), Variable(), Sequence("(", Expression(), ")"));
    }


    Rule Function() {
        return Sequence(Identifier(), "(", ArgumentList(), ")");
    }

    Rule ArgumentList() {
        return repsep(Expression(), ArgumentSeparator());
    }

    @SuppressNode
    Rule ArgumentSeparator() {
        return FirstOf(",", ";");
    }

    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(StringLiteral(), FloatLiteral(), IntLiteral());
    }

    // Rules for literals
    // WARNING: all literals should care about matching whitespace
    
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


    @Override
    Rule Identifier() {
        return Sequence(Letter(), ZeroOrMore(LetterOrDigit()), Whitespace());
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
