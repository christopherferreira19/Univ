grammar niTokens;

// Structure

ORB : '(' ;
CRB : ')' ;
OCB : '{' ;
CCB : '}' ;
OSB : '[' ;
CSB : ']' ;

COMMA : ',' ;
COLON : ':' ;
SEMICOLON: ';' ;

RETURN_TYPE : '->' ;

DASH: '#' ;

// Assign

ASSIGN        : '=' ;
ASSIGN_PLUS   : '+=' ;
ASSIGN_MINUS  : '-=' ;
ASSIGN_TIMES  : '*=' ;
ASSIGN_DIVIDE : '/=' ;
ASSIGN_MODULO : '%=' ;
ASSIGN_AND    : 'and=' ;
ASSIGN_OR     : 'or=' ;

assign : (ASSIGN | ASSIGN_PLUS | ASSIGN_MINUS | ASSIGN_TIMES | ASSIGN_DIVIDE | ASSIGN_MODULO) ;

// Keyword and Operators

VAL    : 'val' ;
VAR    : 'var' ;
FN     : 'fn' ;
RETURN : 'return' ;
IF     : 'if' ;
ELSE   : 'else' ;
WHILE  : 'while' ;
LOOP   : 'loop' ;

NOT : 'not' ;
AND : 'and' ;
OR  : 'or' ;

EQUAL         : '==' ;
DIFFERENT     : '!=' ;
LESS_THAN     : '<' ;
LESS_EQUAL    : '<=' ;
GREATER_THAN  : '>' ;
GREATER_EQUAL : '>=' ;

compare: (LESS_THAN | LESS_EQUAL | GREATER_THAN | GREATER_EQUAL) ;

PLUS   : '+' ;
MINUS  : '-' ;
TIMES  : '*' ;
DIVIDE : '/' ;
MODULO : '%' ;

// Type Keywords and Operators
INTER   : '&' ;
UNION   : '|' ;
FN_TYPE : 'Fn' ;

// Literal
TRUE    : 'true' ;
FALSE   : 'false' ;
INTEGER : [0-9]+ ;
FLOAT   : [0-9][0-9_]*[\.][0-9_]* ;
CHAR    : ['].['] ;
STRING  : ["].*?["] ;

// Identifier
VAR_IDF  : [a-z][a-zA-Z0-9_]* ;
TYPE_IDF : [A-Z][a-zA-Z0-9_]* ;
