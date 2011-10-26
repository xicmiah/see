package see.parser.grammar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.errors.ParsingException;
import org.parboiled.support.Var;
import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.parser.config.FunctionResolver;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.immutable.ImmutableConstNode;
import see.tree.immutable.ImmutableFunctionNode;
import see.tree.immutable.ImmutableVarNode;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"InfiniteRecursion"})
class Expressions extends AbstractGrammar {
    final Literals literals;

    final NumberFactory numberFactory;
    final FunctionResolver functions;

    final Character argumentSeparator;

    final Set<String> keywords = ImmutableSet.of("if", "then", "else", "return");

    Expressions(GrammarConfiguration config) {
        numberFactory = config.getNumberFactory();
        functions = config.getFunctions();
        
        argumentSeparator = numberFactory.getDecimalSeparator() == ',' ? ';' : ',';
        literals = Parboiled.createParser(Literals.class, numberFactory.getDecimalSeparator());
    }

    Rule ReturnExpression() {
        NodeListVar statements = new NodeListVar();
        return Sequence(
                ExpressionList(), statements.append(pop()),
                "return", RightExpression(), Optional(";"), statements.append(pop()),
                push(makeSeqNode(statements.get()))
        );
    }

    /**
     * List of zero or more terms. Pushes one node.
     * @return rule
     */
    Rule ExpressionList() {
        NodeListVar statements = new NodeListVar();
        return Sequence(
                ZeroOrMore(Term(), statements.append(pop())),
                push(makeSeqNode(statements.get()))
        );
    }

    /**
     * A if..then..else or expression ending with semicolon.
     * Pushes it's value to stack
     * @return rule
     */
    Rule Term() {
        return FirstOf(Conditional(), Sequence(Expression(), ";"));
    }

    /**
     * Wraps list of expressions in FunctionNode with Sequence function
     * Short-circuits if list has only one element.
     * Returns seq node with empty arguments if expressions are empty.
     * Expects sequence function to map to operator ';'
     * @param statements list of expressions to wrap
     * @return constructed node
     */
    Node<Object> makeSeqNode(ImmutableList<Node<Object>> statements) {
        return statements.size() == 1 ? statements.get(0) : makeFNode(";", statements);
    }

    Rule Expression() {
        return FirstOf(PropertyAssignment(), VariableAssignment(), RightExpression());
    }

    /**
     * Assignment to variable. Pushes one node.
     * @return constructed rule
     */
    Rule VariableAssignment() {
        return Sequence(VarName(), "=", Expression(), pushBinOp("="));
    }

    /**
     * Assignment to property. Pushes one node.
     * Treated differently from variable assignment, as it doesn't require context access.
     * @return constructed rule
     */
    Rule PropertyAssignment() {
        return Sequence(PropertyAccess(), "=", Expression(),
                swap3() && push(makeFNode(".=", ImmutableList.of(pop(), pop(), pop()))));
    }

    /**
     * Special form. Matches variable, pushes variable name.
     * @return rule
     */
    Rule VarName() {
        return Sequence(Variable(), drop() && push(new ImmutableConstNode<Object>(matchTrim())));
    }

    Rule Conditional() {
        NodeListVar args = new NodeListVar();
        return Sequence(
                "if", "(", RightExpression(), args.append(pop()), ")",
                "then", "{", ExpressionList(), args.append(pop()), "}",
                Optional("else", "{", ExpressionList(), args.append(pop()), "}"),
                push(makeFNode("if", args.get()))
        );
    }

    Rule RightExpression() {
        return OrExpression();
    }

    Rule OrExpression() {
        return repeatWithOperator(AndExpression(), "||");
    }

    Rule AndExpression() {
        return repeatWithOperator(EqualExpression(), "&&");
    }

    Rule EqualExpression() {
        return repeatWithOperator(RelationalExpression(), FirstOf("!=", "=="));
    }

    Rule RelationalExpression() {
        return repeatWithOperator(AdditiveExpression(), FirstOf("<=", ">=", "<", ">"));
    }

    Rule AdditiveExpression() {
        return repeatWithOperator(MultiplicativeExpression(), FirstOf("+", "-"));
    }

    Rule MultiplicativeExpression() {
        return repeatWithOperator(UnaryExpression(), FirstOf("*", "/"));
    }

    Rule UnaryExpression() {
        Var<String> op = new Var<String>("");
        return FirstOf(
                Sequence(AnyOf("+-!"), op.set(matchTrim()), UnaryExpression(), push(makeUNode(op.get(), pop()))),
                PowerExpression()
        );
    }

    Rule PowerExpression() {
        return Sequence(UnaryExpressionNotPlusMinus(),
                Optional("^", UnaryExpression(), pushBinOp("^")));
    }

    Rule UnaryExpressionNotPlusMinus() {
        return FirstOf(
                Constant(),
                SpecialForm(),
                Function(),
                PropertyRead(),
                Variable(),
                Sequence("(", Expression(), ")")
        );
    }

    Rule PropertyRead() {
        return Sequence(PropertyAccess(), pushBinOp("."));
    }

    Rule PropertyAccess() {
        return Sequence(Variable(), ".", PropertyChain());
    }

    /**
     * A sequence of identifiers, joined with ".".
     * Pushes itself as const string node.
     * @return constructed rule
     */
    Rule PropertyChain() {
        return Sequence(rep1sep(Identifier(), "."), push(new ImmutableConstNode<Object>(matchTrim())));
    }

    /**
     * Repeat rule with separator, combining results into binary tree.
     * Matches like rep1sep, but combines results.
     * @param rule rule to match. Expected to push one node.
     * @param separator separator between rules
     * @return rule
     */
    Rule repeatWithOperator(Rule rule, Object separator) {
        Var<String> operator = new Var<String>("");
        return Sequence(rule,
                ZeroOrMore(separator, operator.set(matchTrim()),
                        rule,
                        pushBinOp(operator.get()))
        );
    }

    /**
     * Combines two entries on top of the stack into FunctionNode with specified operator
     * @param operator function name
     * @return true if operation succeded
     */
    boolean pushBinOp(String operator) {
        return swap() && push(makeFNode(operator, ImmutableList.of(pop(), pop())));
    }

    /**
     * Construct unary function node.
     * Short-circuits for unary plus.
     * @param operator unary operator
     * @param expr operator argument
     * @return constructed node
     */
    Node<Object> makeUNode(String operator, Node<Object> expr) {
        if (operator.equals("+")) {
            return expr;
        } else {
            return makeFNode(operator, ImmutableList.of(expr));
        }
    }

    /**
     * Construct function node with resolved function
     * @param name function name
     * @param args argument list
     * @return constructed node
     */
    FunctionNode<Object, Object> makeFNode(String name, ImmutableList<Node<Object>> args) {
        ContextCurriedFunction<Function<List<Object>,Object>> function = functions.get(name);
        if (function == null) {
            throw new ParsingException("Function not found: " + name);
        }
        return new ImmutableFunctionNode<Object, Object>(function, args);
    }

    /**
     * Constant. Pushes ImmutableConstNode(value)
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
        Var<String> function = new Var<String>("");
        NodeListVar args = new NodeListVar();
        return Sequence(
                FirstOf(Identifier(), "if"),
                function.set(matchTrim()),
                ArgumentList(args),
                push(makeFNode(function.get(), args.get()))
        );
    }

    /**
     * A special form.
     * @return rule
     */
    Rule SpecialForm() {
        return IsDefined();
    }

    /**
     * Special form for isDefined function.
     * Matches like function application, but requires one Variable inside.
     * @return rule
     */
    Rule IsDefined() {
        return Sequence(
                "isDefined",
                "(", VarName(), ")",
                push(makeFNode("isDefined", ImmutableList.of(pop())))
        );
    }

    Rule ArgumentList(NodeListVar args) {
        return Sequence("(", repsep(Sequence(Expression(), args.append(pop())), ArgumentSeparator()), ")");
    }

    Rule ArgumentSeparator() {
        return Sequence(Ch(argumentSeparator), Whitespace());
    }

    Rule Variable() {
        return Sequence(Identifier(), push(new ImmutableVarNode<Object>(matchTrim())));
    }

    /**
     * String literal. Expected to push it's value/
     * @return rule
     */
    Rule String() {
        return Sequence(literals.StringLiteral(), push(new ImmutableConstNode<Object>(stripQuotes(matchTrim()))));
    }

    /**
     * Floating point literal. Expected to push it's value
     * @return rule
     */
    Rule Float() {
        return Sequence(literals.FloatLiteral(), push(new ImmutableConstNode<Object>(matchNumber())));
    }

    /**
     * Integer literal. Expected to push it's value.
     * @return constructed rule
     */
    Rule Int() {
        return Sequence(literals.IntLiteral(), push(new ImmutableConstNode<Object>(matchNumber())));
    }

    @SuppressSubnodes
    Rule Identifier() {
        return Sequence(Name(), !keywords.contains(matchTrim()));
    }

    Rule Name() {
        return Sequence(literals.Letter(), ZeroOrMore(literals.LetterOrDigit()), Whitespace());
    }

    Number matchNumber() {
        return numberFactory.getNumber(matchTrim());
    }

    /**
     * Return input without first and last character.
     * I.e. "str" -> str
     * @param input input string
     * @return truncated input
     */
    String stripQuotes(String input) {
        return input.substring(1, input.length() - 1);
    }
}
