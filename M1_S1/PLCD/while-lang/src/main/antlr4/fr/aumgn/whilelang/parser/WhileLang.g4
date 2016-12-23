grammar WhileLang;

WS: [ \t\r\n]+ -> skip ;
Boolean: 'true' | 'false' ;
Numeral: [0-9]+ ;
Identifier : [a-z][A-Za-z0-9_]* ;
Type: [A-Z][A-Za-z0-9_]* ;

program : block ;

block : (declaration ';')* statement* ;

declaration
    : 'var' Identifier ':' Type  # DeclarationType
    | 'var' Identifier '=' expression  # Definition
    ;

statement
    : 'begin' block 'end'  # BlockStatement
    | 'if' expression 'then' thenBody=block 'else' elseBody=block 'end'  # If
    | 'while' expression 'do' block 'end'  # While
    | Identifier '=' expression ';' # Assignement
    | 'print' '(' expression ')' ';' # Print
    ;

expression
	: left=expression op=('*' | '/' | '%') right=expression # Multiplication
	| left=expression op=('+' | '-') right=expression # Addition
	| left=expression_operande op=('<' | '<=' | '>' | '>=') right=expression_operande # Comparison
	| left=expression op=('==' | '!=') right=expression # Equality
	| left=expression op='and' right=expression # Disjunction
	| left=expression op='or' right=expression # Conjunction
	| expression_operande # Atom
	;

expression_operande
	: 'not' expression_operande # UnaryNot
	| '+' expression_operande # UnaryPlus
	| '-' expression_operande # UnaryMinus
	| '(' expression ')' # Parenthesis
	| Identifier # Identifier
	| Boolean # Boolean
	| Numeral # Number
	;