grammar NiLang;

import niTokens;

WS: [ \r\t\n]+ -> skip ;

ni : statements ;


// Statements
statement
    : (VAL | VAR) VAR_IDF COLON type SEMICOLON                   # Declaration
    | (VAL | VAR) VAR_IDF assign expression SEMICOLON # Definition
    | VAR_IDF assign expression SEMICOLON             # Assignement
    | RETURN expression SEMICOLON                                # Return
    | IF cond=expression thenBlock=block (ELSE elseBlock=block)? # If
    | WHILE cond=expression block                                # While
    | LOOP block WHILE cond=expression SEMICOLON                 # Loop
    | FN VAR_IDF parameters=fnSignature block                    # FnStatement
    | expression SEMICOLON                                       # ExpressionStatement
    ;

statements : (statement)* ;
block : OCB statements CCB ;
argument : VAR_IDF COLON type ;
arguments : argument (COMMA argument)* ;
returnType : RETURN_TYPE type ;
fnSignature : (ORB arguments? CRB)? returnType? ;


// Expressions
expression
 	: left=expression  operator=(TIMES | DIVIDE | MODULO) right=expression # Binary
 	| left=expression  operator=(PLUS | MINUS)            right=expression # Binary
 	| left=atom        compare                            right=atom       # Comparison
 	| left=expression  operator=(EQUAL | DIFFERENT)       right=expression # Binary
 	| left=expression  operator=AND                       right=expression # Binary
 	| left=expression  operator=OR                        right=expression # Binary
	| atom # AsAtom
	;

expressions : expression (COMMA expression)* ;

atom
	: operator=(NOT | PLUS | MINUS) operande=atom  # Unary
	| ORB expressions CRB                          # Tuple
	| FN parameters=fnSignature block              # FnExpression
	| fn=atom ORB parameters=expressions? CRB      # Call
	| VAR_IDF                                      # Variable
	| TRUE                                         # True
	| FALSE                                        # False
	| INTEGER                                      # Integer
	| FLOAT                                        # Float
	| CHAR                                         # Char
	| STRING                                       # String
	;


// Types
type
    : type UNION type                                      # UnionType
    | type INTER type                                      # InterType
    | FN_TYPE (ORB types? CRB)? (RETURN_TYPE type)?        # FnType
    | ORB types? CRB                                       # TupleType
    | TYPE_IDF (LESS_THAN types GREATER_THAN)?             # IdentifierType
    ;

types: type (COMMA type)* ;