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

import see.exceptions.ParseException;
}

@lexer::members{

@Override
public void reportError(RecognitionException e) {
    throw new ParseException(ExceptionConverter.createLexerErrorDescription(e, this, state), e);
}

@Override
public void recover(RecognitionException e) {
    throw new ParseException(ExceptionConverter.createLexerErrorDescription(e, this, state), e);
}

}


@parser::members{

    public <T> Node<T> singleExpression() throws RecognitionException {
        return (Node<T>) conditionalExpression();
    }

    public <T> Node<T> multipleExpressions() throws RecognitionException {
        return (Node<T>) calculationExpression();
    }

}

functionParamSeparator
	:	',' ;

fragment DECIMAL_SEPARATOR
	:	 '.';



