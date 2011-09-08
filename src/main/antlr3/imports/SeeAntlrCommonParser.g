parser grammar SeeAntlrCommonParser;

options {
  language=Java;
  // We're going to output an AST.
  output = AST;
}

// These are imaginary tokens that will serve as parent nodes
// for grouping other tokens in our AST.
tokens {
	FUNCTION_CALL;
	OPERATOR;
	VARIABLE;
	UNAR;
	ASSIGN;
	IF_ST;
	BLOCK;
	RETURN_KW;
}


/* ***************  Grammar. Lexer and Parser rules *************** */

//start rule for full script formulas
calculationExpression
	:	s=script? (r=RETURN rexpr=right_expression (EOF | SEMICOLON))? -> $s? ^(RETURN_KW[$r] $rexpr)?;

//start rule for one expression formulas that is used in condition input
conditionalExpression
	:	right_expression EOF!;

script: (common_statement)+;

common_statement
	:	liner_statement
	|	ifSt=IF LEFT_PAREN  cond=right_expression RIGHT_PAREN
		th=THEN LEFT_BRACE thexpr=script? RIGHT_BRACE
		( el=ELSE LEFT_BRACE elexpr=script? RIGHT_BRACE -> ^(IF_ST[$ifSt] $cond ^(BLOCK[$th] $thexpr?) ^(BLOCK[$el] $elexpr?))
		|                     							  -> ^(IF_ST[$ifSt] $cond ^(BLOCK[$th] $thexpr?))
		)
	;

liner_statement
	:	id=ID ao=ASSIGN_OP ce=liner_statement -> ^(ASSIGN[$ao] VARIABLE[$id] $ce)
	|	right_expression (EOF! | SEMICOLON!)
	;

right_expression
	:	 orLogical;

orLogical
	:	(a=andLogical->$a)
		( op=LOGICAL_OR b=andLogical
			-> ^(OPERATOR[$op] $orLogical $b))*;

andLogical
	:	(a=eqNeqLogical->$a)
		(op=LOGICAL_AND b=eqNeqLogical
			-> ^(OPERATOR[$op] $andLogical $b))*;

eqNeqLogical
	:	(a=compareLogical->$a)
		(op=LOGICAL_EQ_NEQ b=compareLogical
			-> ^(OPERATOR[$op] $eqNeqLogical $b))*;

compareLogical
	:	(a=additive->$a)
		(op=LOGICAL_LESS_MORE b=additive
			-> ^(OPERATOR[$op] $compareLogical $b))*;

additive
	:	(a=multiplicative->$a)
		(op=SIGN b=multiplicative
			-> ^(OPERATOR[$op] $additive $b))*;

multiplicative
	:	(a=unar->$a)
		(op=MULT_DIV b=unar
			-> ^(OPERATOR[$op] $multiplicative $b))*;

unar
	:	(op=LOGICAL_NOT | op=SIGN) unar -> ^(UNAR[$op] unar)
	|	(a=atom->$a)
		(op=POW b=unar
			-> ^(OPERATOR[$op] $a $b))?
	;

atom
	:	constant
	|	func
	|	id=ID -> VARIABLE[$id]
	|	LEFT_PAREN right_expression RIGHT_PAREN -> right_expression
	;

constant
	:	NUMBER | STRING;

func
	:	id=ID LEFT_PAREN functionParams? RIGHT_PAREN -> ^(FUNCTION_CALL[$id] ID LEFT_PAREN RIGHT_PAREN functionParams?)
	;

functionParams
	:	right_expression ( functionParamSeparator! right_expression)*
	;
	

/*			LIXER RULES			*/





