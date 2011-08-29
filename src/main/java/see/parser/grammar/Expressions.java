package see.parser.grammar;

import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class Expressions extends AbstractGrammar {
    private final Literals literals = Parboiled.createParser(Literals.class);

    public Rule CalcExpression() {
        return Sequence(ExpressionList(), "return", RightExpression(), EOI);
    }

    public Rule Condition() {
        return Sequence(RightExpression(), EOI);
    }

    Rule ExpressionList() {
        return Sequence(Expression(), ZeroOrMore(";", Optional(Expression())));
    }

    Rule Expression() {
        return FirstOf(AssignExpression(), Conditional(), RightExpression());
    }

    Rule AssignExpression() {
        return Sequence(Variable(), ";", Expression());
    }

    Rule Conditional() {
        return Sequence("if", "(", RightExpression(), ")",
                "then", "{", ExpressionList(), "}",
                Optional("else", "{", ExpressionList(), "}"));
    }

    Rule Variable() {
        return Identifier();
    }

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
        return fromStringLiteral(",");
    }

    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(literals.StringLiteral(), literals.FloatLiteral(), literals.IntLiteral());
    }

    @SuppressSubnodes
    Rule Identifier() {
        return Sequence(literals.Letter(), ZeroOrMore(literals.LetterOrDigit()), Whitespace());
    }

}
