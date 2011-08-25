package see.parser;

import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class FullGrammar extends MacroGrammar {
    @Override
    Rule RightExpression() {
        return OrExpression();
    }

    Rule OrExpression() {
        return Sequence(AndExpression(), ZeroOrMore("||", AndExpression()));
    }

    Rule AndExpression() {
        return Sequence(EqualExpression(), ZeroOrMore("&&", EqualExpression()));
    }

    Rule EqualExpression() {
        return Sequence(RelationalExpression(), ZeroOrMore(FirstOf("!=", "=="), RelationalExpression()));
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
        return FirstOf(Constant(), Function(), Variable());
    }


    Rule Function() {
        return Sequence(Identifier(), "(", ArgumentList(), ")");
    }

    Rule ArgumentList() {
        return Sequence(Expression(), ZeroOrMore(ArgumentSeparator(), Expression()));
    }

    Rule ArgumentSeparator() {
        return FirstOf(",", ";");
    }


    Rule Constant() {
        return FirstOf(StringLiteral(), NumericLiteral());
    }

    Rule StringLiteral() {
        return Sequence(Ch('"'), ZeroOrMore(Sequence(TestNot("\""), ANY)).suppressSubnodes(), Ch('"'), Whitespace());
    }

    Rule NumericLiteral() {
        return OneOrMore(CharRange('0', '9'), Whitespace());
    }


    @Override
    Rule Identifier() {
        return Sequence(Letter(), ZeroOrMore(LetterOrDigit()), Whitespace());
    }

    Rule Letter() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '-');
    }

    Rule LetterOrDigit() {
        return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '-', CharRange('0', '9'));
    }

    Rule rep1sep(Object rule, Object separator) {
        return Sequence(rule, ZeroOrMore(separator, rule));
    }
}
