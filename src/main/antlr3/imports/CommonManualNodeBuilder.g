parser grammar CommonManualNodeBuilder;

options {
  language=Java;
}

/*
@parser::header{
package see.parser.antlr;

import see.parser.antlr.tree.SeeTreeNode;
import see.tree.Node;
import java.util.List;
import java.util.LinkedList;
}
*/


@parser::members{
    private SeeNodesFactory nodesFactory;


    public void setNodesFactory(SeeNodesFactory factory){
        this.nodesFactory = factory;
    }


}


/* ***************  Grammar. Lexer and Parser rules *************** */

//start rule for full script formulas
calculationExpression returns [Node<Object> node]
    @init{
        List<Node<Object>> expressionList = new LinkedList<Node<Object>>();
    }
	:	(s=script {expressionList.addAll($s.exprList);})?
	(r=RETURN rexpr=right_expression {expressionList.add($rexpr.node);} (EOF | SEMICOLON))?
	{
	    if (!expressionList.isEmpty()){
	        if (expressionList.size() == 1){
                $node = expressionList.get(0);
	        }else{
                $node = nodesFactory.createSequence(expressionList);
	        }
	    }else{
	        $node = nodesFactory.createUndefinedNode();
	    }
	}
	;

//start rule for one expression formulas that is used in condition input
conditionalExpression returns [Node<Object> node]
	:	r_exp=right_expression EOF {$node = $r_exp.node;};

script returns [List<Node<Object>> exprList]
    @init{
        $exprList = new LinkedList<Node<Object>>();
    }
    :
    (cs=common_statement {$exprList.add($cs.node);})+ ;

common_statement returns [Node node]
	:	ls=liner_statement {$node = $ls.node;}
	|	ifSt=IF LEFT_PAREN  cond=right_expression RIGHT_PAREN
		th=THEN LEFT_BRACE thexpr=script? RIGHT_BRACE
		( el=ELSE LEFT_BRACE elexpr=script? RIGHT_BRACE {} )?
		{
		    Node<Object> thenNode = null;
		    if ($thexpr.exprList != null && !$thexpr.exprList.isEmpty()){
		        thenNode = nodesFactory.createSequence($thexpr.exprList);
		    }
		    Node<Object> elseNode = null;
		    if ( $elexpr.exprList != null && !$elexpr.exprList.isEmpty()){
		        elseNode = nodesFactory.createSequence($elexpr.exprList);
		    }
		    $node = nodesFactory.createIfNode($ifSt, $cond.node, thenNode, elseNode);
		}
	;

liner_statement returns [Node<Object> node]
	:	id=ID ao=ASSIGN_OP ce=liner_statement
	    {
	    Node<Object> assignNode = nodesFactory.createVarNode($id);
	    $node = nodesFactory.createOperatorNode($ao, assignNode, $ce.node);
	    }
	|	r_exp=right_expression (EOF | SEMICOLON) {$node = $r_exp.node;}
	;

right_expression returns [Node<Object> node]
	:	 ol=orLogical {$node = $ol.node;};

orLogical returns [Node<Object> node]
	:	(a=andLogical {$node = $a.node;})
		( op=LOGICAL_OR b=andLogical {$node = nodesFactory.createOperatorNode($op, $node, $b.node);})*;

andLogical returns [Node<Object> node]
	:	(a=eqNeqLogical {$node = $a.node;})
		(op=LOGICAL_AND b=eqNeqLogical {$node = nodesFactory.createOperatorNode($op, $node, $b.node);})*;

eqNeqLogical returns [Node<Object> node]
	:	(a=compareLogical {$node = $a.node;})
		(op=LOGICAL_EQ_NEQ b=compareLogical {$node = nodesFactory.createOperatorNode($op, $node, $b.node);})*;

compareLogical returns [Node<Object> node]
	:	(a=additive {$node = $a.node;})
		(op=LOGICAL_LESS_MORE b=additive {$node = nodesFactory.createOperatorNode($op, $node, $b.node);})*;

additive returns [Node<Object> node]
	:	(a=multiplicative {$node = $a.node;})
		(op=SIGN b=multiplicative {$node = nodesFactory.createOperatorNode($op, $node, $b.node);})*;

multiplicative returns [Node<Object> node]
	:	(a=unar {$node = $a.node;})
		(op=MULT_DIV b=unar {$node = nodesFactory.createOperatorNode($op, $node, $b.node);})*;

unar returns [Node<Object> node]
	:	(op=LOGICAL_NOT | op=SIGN) u=unar
	{
		if ($op.text.equals("+")){
			$node = $u.node;
		}else{
			$node = nodesFactory.createOperatorNode($op, $u.node);
		}
	}
	|	(a=atom {$node = $a.node;})
		(op=POW b=unar
			{$node = nodesFactory.createOperatorNode($op, $a.node, $b.node);})?
	;

atom returns [Node<Object> node]
	:	c=constant {$node = $c.node;}
	|	f=func {$node = $f.node;}
	|	id=ID {$node = nodesFactory.createVarNode($id);}
	|	LEFT_PAREN r_exp=right_expression RIGHT_PAREN {$node = $r_exp.node;}
	;

constant returns [Node<Object> node]
	:	num=NUMBER {$node = nodesFactory.createNumberNode($num);}
	|   str=STRING {$node = nodesFactory.createStringNode($str);};

func returns [Node<Object> node]
	:	id=ID LEFT_PAREN params=functionParams? RIGHT_PAREN
	{$node = nodesFactory.createFunctionNode($id, $params.functionArgs);}
	;

functionParams returns [List<Node<Object>> functionArgs]

    @init{
        $functionArgs = new LinkedList<Node<Object>>();
    }

	:	arg=right_expression {$functionArgs.add($arg.node);}( functionParamSeparator otherArg=right_expression {$functionArgs.add($otherArg.node);})*
	;