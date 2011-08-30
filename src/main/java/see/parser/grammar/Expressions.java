package see.parser.grammar;

import com.google.common.base.Function;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.common.ImmutableList;
import org.parboiled.support.Var;
import see.evaluator.DoubleNumberFactory;
import see.evaluator.NumberFactory;
import see.functions.ContextCurriedFunction;
import see.parser.FunctionResolver;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.VarNode;

import java.util.List;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class Expressions extends AbstractGrammar {
    final Literals literals = Parboiled.createParser(Literals.class);

    // TODO: add proper injection
    final NumberFactory numberFactory = new DoubleNumberFactory();
    final FunctionResolver functions = new FunctionResolver();

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

    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(String(), Float(), Int());
    }

    Rule Function() {
        Var<ContextCurriedFunction<Function<List<Object>, Object>>> function = new Var<ContextCurriedFunction<Function<List<Object>, Object>>>();
        Var<ImmutableList<Node<Object>>> args = new Var<ImmutableList<Node<Object>>>(ImmutableList.<Node<Object>>of());
        return Sequence(
                Identifier(),
                function.set(functions.get(match())),
                "(", ArgumentList(args), ")",
                push(new FunctionNode<Object, Object>(function.get(), args.get()))
        );
    }


    Rule ArgumentList(Var<ImmutableList<Node<Object>>> args) {
        return repsep(
                Sequence(Expression(), args.set(ImmutableList.<Node<Object>>of(args.get(), pop()))),
                ArgumentSeparator()
        );
    }

    @SuppressNode
    Rule ArgumentSeparator() {
        return fromStringLiteral(",");
    }

    Rule Variable() {
        return Sequence(Identifier(), push(new VarNode<Object>(match().trim())));
    }

    Rule String() {
        return Sequence(literals.StringLiteral(), push(new ConstNode<Object>(match().trim())));
    }

    Rule Float() {
        return Sequence(literals.FloatLiteral(), push(new ConstNode<Object>(matchNumber())));
    }

    /**
     * A constant literal. Expected to push it's value.
     * @return constructed rule
     */
    Rule Int() {
        return Sequence(literals.IntLiteral(), push(new ConstNode<Object>(matchNumber())));
    }

    @SuppressSubnodes
    Rule Identifier() {
        return Sequence(literals.Letter(), ZeroOrMore(literals.LetterOrDigit()), Whitespace());
    }

    Number matchNumber() {
        return numberFactory.getNumber(match());
    }
}
