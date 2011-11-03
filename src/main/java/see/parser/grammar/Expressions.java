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
import see.tree.VarNode;
import see.tree.immutable.ImmutableConstNode;
import see.tree.immutable.ImmutableFunctionNode;
import see.tree.immutable.ImmutableVarNode;

import java.util.List;
import java.util.Set;

import static see.parser.grammar.PropertyAccess.Simple;

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

    @Terminal
    Rule ReturnExpression() {
        ListVar<Node<Object>> statements = new ListVar<Node<Object>>();
        return Sequence(
                ExpressionList(), statements.append(pop()),
                T("return"), RightExpression(), Optional(T(";")), statements.append(pop()),
                push(makeSeqNode(statements.get()))
        );
    }

    /**
     * List of zero or more terms. Pushes one node.
     * @return rule
     */
    @Terminal
    Rule ExpressionList() {
        ListVar<Node<Object>> statements = new ListVar<Node<Object>>();
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
    @Terminal
    Rule Term() {
        return FirstOf(Conditional(), Sequence(Expression(), T(";")));
    }

    /**
     * Wraps list of expressions in FunctionNode with Sequence function
     * Short-circuits if list has only one element.
     * Returns seq node with empty arguments if expressions are empty.
     * Expects sequence function to map to operator ';'
     * @param statements list of expressions to wrap
     * @return constructed node
     */
    Node<Object> makeSeqNode(List<Node<Object>> statements) {
        return statements.size() == 1 ? statements.get(0) : makeFNode(";", statements);
    }

    @Terminal
    Rule Expression() {
        return FirstOf(PropertyAssignment(), VariableAssignment(), RightExpression());
    }

    /**
     * Assignment to variable. Pushes one node.
     * @return constructed rule
     */
    @Terminal
    Rule VariableAssignment() {
        return Sequence(VarName(), T("="), Expression(), pushBinOp("="));
    }

    /**
     * Assignment to property. Pushes one node.
     * Treated differently from variable assignment, as it doesn't require context access.
     * @return constructed rule
     */
    @Terminal
    Rule PropertyAssignment() {
        ListVar<Node<?>> props = new ListVar<Node<?>>();
        return Sequence(PropertyAccess(props), T("="), Expression(), props.append(makeUNode("props.target", pop())),
                push(makeFNode(".=", props.get())));
    }

    /**
     * Special form. Matches variable, pushes variable name.
     * @return rule
     */
    @Terminal
    Rule VarName() {
        return Sequence(Variable(), push(new ImmutableConstNode<Object>(getVarName((VarNode<?>) pop()))));
    }

    String getVarName(VarNode<?> node) {
        return node.getName();
    }

    @Terminal
    Rule Conditional() {
        ListVar<Node<Object>> args = new ListVar<Node<Object>>();
        return Sequence(
                T("if"), T("("), RightExpression(), args.append(pop()), T(")"),
                Block(), args.append(pop()),
                Optional(T("else"), Block(), args.append(pop())),
                push(makeFNode("if", args.get()))
        );
    }

    @Terminal
    Rule Block() {
        return FirstOf(
                Sequence(T("{"), ExpressionList(), T("}")),
                Term()
        );
    }

    @Terminal
    Rule RightExpression() {
        return OrExpression();
    }

    @Terminal
    Rule OrExpression() {
        return repeatWithOperator(AndExpression(), "||");
    }

    @Terminal
    Rule AndExpression() {
        return repeatWithOperator(EqualExpression(), "&&");
    }

    @Terminal
    Rule EqualExpression() {
        return repeatWithOperator(RelationalExpression(), FirstOf("!=", "=="));
    }

    @Terminal
    Rule RelationalExpression() {
        return repeatWithOperator(AdditiveExpression(), FirstOf("<=", ">=", "<", ">"));
    }

    @Terminal
    Rule AdditiveExpression() {
        return repeatWithOperator(MultiplicativeExpression(), FirstOf("+", "-"));
    }

    @Terminal
    Rule MultiplicativeExpression() {
        return repeatWithOperator(UnaryExpression(), FirstOf("*", "/"));
    }

    @Terminal
    Rule UnaryExpression() {
        Var<String> op = new Var<String>("");
        return FirstOf(
                Sequence(
                        T(AnyOf("+-!"), op.set(match())),
                        UnaryExpression(),
                        push(makeUNode(op.get(), pop()))),
                PowerExpression()
        );
    }

    Rule PowerExpression() {
        return Sequence(PropertyRead(),
                Optional(T("^"), UnaryExpression(), pushBinOp("^")));
    }

    @Terminal
    Rule PropertyRead() {
        ListVar<Node<?>> props = new ListVar<Node<?>>();
        return FirstOf(
                Sequence(PropertyAccess(props), push(makeFNode(".", props.get()))),
                Atom()
        );
    }

    @Terminal
    Rule PropertyAccess(ListVar<? super Node<?>> props) {
        return Sequence(Atom(), props.append(makeUNode("props.target", pop())),
                OneOrMore(FirstOf(
                        T(".", Identifier(), props.append(new ImmutableConstNode<Simple>(new Simple(match())))),
                        Sequence(T("["), RightExpression(), props.append(makeUNode("[]", pop())), T("]"))
                ))
        );
    }

    @Terminal
    Rule Atom() {
        return FirstOf(
                Constant(),
                SpecialForm(),
                Function(),
                Variable(),
                Sequence(T("("), Expression(), T(")"))
        );
    }

    /**
     * Repeat rule with separator, combining results into binary tree.
     * Matches like rep1sep, but combines results.
     * @param rule rule to match. Expected to push one node.
     * @param separator separator between rules
     * @return rule
     */
    @Terminal
    Rule repeatWithOperator(Rule rule, Object separator) {
        Var<String> operator = new Var<String>("");
        return Sequence(rule,
                ZeroOrMore(
                        T(separator, operator.set(match())),
                        rule,
                        pushBinOp(operator.get())
                )
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
    FunctionNode<Object, Object> makeFNode(String name, List<? extends Node<?>> args) {
        ContextCurriedFunction<Function<List<Object>,Object>> function = functions.get(name);
        if (function == null) {
            throw new ParsingException("Function not found: " + name);
        }
        return new ImmutableFunctionNode<Object, Object>(function, (List<Node<Object>>) args);
    }

    /**
     * Constant. Pushes ImmutableConstNode(value)
     * @return rule
     */
    @Terminal
    @SuppressSubnodes
    Rule Constant() {
        return FirstOf(String(), Float(), Int(), Boolean());
    }

    /**
     * Function application. Pushes FunctionNode(f, args).
     * @return rule
     */
    @Terminal
    Rule Function() {
        Var<String> function = new Var<String>("");
        ListVar<Node<Object>> args = new ListVar<Node<Object>>();
        return Sequence(
                T(FirstOf(Identifier(), "if"), function.set(matchTrim())),
                ArgumentList(args),
                push(makeFNode(function.get(), args.get()))
        );
    }

    /**
     * A special form.
     * @return rule
     */
    @Terminal
    Rule SpecialForm() {
        return IsDefined();
    }

    /**
     * Special form for isDefined function.
     * Matches like function application, but requires one Variable inside.
     * @return rule
     */
    @Terminal
    Rule IsDefined() {
        return Sequence(
                T("isDefined"),
                T("("), VarName(), T(")"),
                push(makeFNode("isDefined", ImmutableList.of(pop())))
        );
    }

    @Terminal
    Rule ArgumentList(ListVar<Node<Object>> args) {
        return Sequence(T("("), repsep(Sequence(Expression(), args.append(pop())), ArgumentSeparator()), T(")"));
    }

    @Terminal
    Rule ArgumentSeparator() {
        return T(argumentSeparator);
    }

    @Terminal
    Rule Variable() {
        return T(Identifier(), push(new ImmutableVarNode<Object>(match())));
    }

    /**
     * String literal. Expected to push it's value/
     * @return rule
     */
    @Terminal
    Rule String() {
        return T(literals.StringLiteral(), push(new ImmutableConstNode<Object>(stripQuotes(match()))));
    }

    /**
     * Floating point literal. Expected to push it's value
     * @return rule
     */
    @Terminal
    Rule Float() {
        return T(literals.FloatLiteral(), push(new ImmutableConstNode<Object>(matchNumber())));
    }

    /**
     * Integer literal. Expected to push it's value.
     * @return constructed rule
     */
    @Terminal
    Rule Int() {
        return T(literals.IntLiteral(), push(new ImmutableConstNode<Object>(matchNumber())));
    }

    @Terminal
    Rule Boolean(){
        return T(literals.BooleanLiteral(), push(new ImmutableConstNode<Object>(Boolean.valueOf(match()))));
    }

    @SuppressSubnodes
    Rule Identifier() {
        return Sequence(Name(), !keywords.contains(match()));
    }

    Rule Name() {
        return Sequence(literals.Letter(), ZeroOrMore(literals.LetterOrDigit()));
    }

    Number matchNumber() {
        return numberFactory.getNumber(match());
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
