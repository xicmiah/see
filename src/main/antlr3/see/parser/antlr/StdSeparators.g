grammar StdSeparators;

options {
  language=Java;
  // We're going to output an AST.
  output = AST;
  superClass=AbstractAntlrGrammarParser;
}


import SeeAntlrCommonLexer, SeeAntlrCommonParser;

@parser::header{
package see.parser.antlr;

import see.parser.antlr.tree.SeeTreeNode;
}

@lexer::header{
package see.parser.antlr;
}

@parser::members{

    public <T> SeeTreeNode<T> singleExpression() throws Exception {
        return (SeeTreeNode<T>) conditionalExpression().getTree();
    }

    public <T> SeeTreeNode<T> multipleExpressions() throws Exception {
        return (SeeTreeNode<T>) calculationExpression().getTree();
    }

}

functionParamSeparator
	:	',' ;

fragment DECIMAL_SEPARATOR 
	:	 '.';
	
	

