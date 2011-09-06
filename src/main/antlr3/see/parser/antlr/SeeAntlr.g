grammar SeeAntlr;

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

@parser::header{
package see.parser.antlr;
}

@lexer::header{
package see.parser.antlr;
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
	:	right_expression ( SEMICOLON! right_expression)*
	;

/*			LIXER RULES			*/
//KEYWORDS
IF 	:	'if';
THEN:	'then';
ELSE:	'else';
RETURN
	:	'return';

SEMICOLON
	:	 ';';

fragment DECIMAL_SEPARATOR
	:	',';

//PARANTHESIS

LEFT_PAREN: '(';
RIGHT_PAREN: ')';

LEFT_BRACE
	:	'{';
RIGHT_BRACE
	:	'}';

//LOGICAL OPERATIONS
LOGICAL_NOT
	:	'!';
LOGICAL_OR
	:	 '||';
LOGICAL_AND
	:	'&&';
LOGICAL_EQ_NEQ
	:	'==' | '!=';
LOGICAL_LESS_MORE : '<' | '>' 	| '<=' | '>=';

//ARITHMETIC OPERATIONS
SIGN: '+' | '-';
MULT_DIV:	'*' | '/' | '%';
POW	:	'^';

//assign op
ASSIGN_OP
	:	'=';
//NUMBERS
NUMBER: INT | FLOAT;


fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';
fragment DIGIT: '0'..'9';

//IDENTIFIER, CAN BE STRING NAME OR VARIABLE NAME

ID  :	(LETTER|'_') (LETTER|DIGIT|'_')*
    ;

//NUMBERS
fragment
INT :	DIGIT+
    ;

fragment
FLOAT
    :   (DIGIT)+ DECIMAL_SEPARATOR (DIGIT)* EXPONENT?
    |   DECIMAL_SEPARATOR (DIGIT)+ EXPONENT?
    |   (DIGIT)+ EXPONENT
    ;
fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

//WHITESPACES
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

//STRING ELEMENTS
STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment
HEX_DIGIT : (DIGIT|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;


