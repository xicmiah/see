package see.parser.grammar;

import com.google.common.collect.ImmutableList;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.Var;
import see.evaluator.DoubleNumberFactory;
import see.evaluator.NumberFactory;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.VarNode;

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

    /**
     * Constant. Pushes ConstNode(value)
     * @return rule
     */
    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(String(), Float(), Int());
    }

    /**
     * Function application. Pushes FunctionNode(f, args).
     * @return rule
     */
    Rule Function() {
        Var<UntypedFunction> function = new Var<UntypedFunction>();
        NodeListVar args = new NodeListVar();
        return Sequence(
                Identifier(),
                function.set(functions.get(matchTrim())),
                "(", ArgumentList(args), ")",
                push(new FunctionNode<Object, Object>(function.get(), args.get()))
        );
    }

    Rule ArgumentList(NodeListVar args) {
        return repsep(Sequence(Expression(), args.append(pop())), ArgumentSeparator());
    }

    @SuppressNode
    Rule ArgumentSeparator() {
        return fromStringLiteral(",");
    }

    Rule Variable() {
        return Sequence(Identifier(), push(new VarNode<Object>(matchTrim())));
    }

    /**
     * String literal. Expected to push it's value/
     * @return rule
     */
    Rule String() {
        return Sequence(literals.StringLiteral(), push(new ConstNode<Object>(matchTrim())));
    }

    /**
     * Floating point literal. Expected to push it's value
     * @return rule
     */
    Rule Float() {
        return Sequence(literals.FloatLiteral(), push(new ConstNode<Object>(matchNumber())));
    }

    /**
     * Integer literal. Expected to push it's value.
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
        return numberFactory.getNumber(matchTrim());
    }
}
