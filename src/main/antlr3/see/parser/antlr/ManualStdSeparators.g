grammar ManualStdSeparators;

options {
  language=Java;
  // We're going to output an AST.
  superClass=AbstractAntlrGrammarParser;
}


import SeeAntlrCommonLexer, CommonManualNodeBuilder;

@parser::header{
package see.parser.antlr;

import see.tree.Node;
import java.util.List;
import java.util.LinkedList;
}

@lexer::header{
package see.parser.antlr;
}

@parser::members{

    public <T> Node<T> singleExpression() throws Exception {
        return (Node<T>) conditionalExpression();
    }

    public <T> Node<T> multipleExpressions() throws Exception {
        return (Node<T>) calculationExpression();
    }

}

functionParamSeparator
	:	',' ;

fragment DECIMAL_SEPARATOR
	:	 '.';



