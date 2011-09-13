lexer grammar SeeAntlrCommonLexer;

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

//KEYWORDS
IF 	:	'if';
THEN:	'then';
ELSE:	'else';
RETURN
	:	'return';

SEMICOLON
	:	 ';';

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

