grammar Rust;

program: functionDefinition+;

functionDefinition: 'fn' identificator '(' functionParameters? ')' functionReturnType? (blockExpression | ';');

functionParameters: functionParameter (',' functionParameter)* ','?;

functionReturnType: '->' type;

functionParameter: identificator ':' type;

type: INT_SUFFIX | 'bool' | 'string' | arraytype ;

arraytype: '[' type ';' INT ']';

blockExpression: '{' statements? '}';

statements: statement+ expression? | expression;

statement: expressionStatement | letStatement;

letStatement: 'let' identificator ':' type? '=' expression ';';

expressionStatement: (expression ';') | (expressionWithBlock ';'?);

expression:
	literalExpression											# LiteralExpression_
	| pathExpression											# PathExpression_
	| expression '.' identificator								# FieldExpression
	| expression '(' (expression (',' expression)* ','?)? ')'	# CallExpression
	| expression '[' expression ']'								# IndexExpression
	| op = ('-' | '!') expression								# NegationExpression
	| expression 'as' type										# TypeCastExpression
	| expression op = ('*' | '/' | '%') expression				# ArithmeticOrLogicalExpression
	| expression op = ('+' | '-') expression					# ArithmeticOrLogicalExpression
	| expression comparisonOperator expression				    # ComparisonExpression
	| expression op = ('&&' | '||') expression					# LazyBooleanExpression
	| expression op = '=' expression							# AssignmentExpression
	| expression compoundAssignOperator expression				# CompoundAssignmentExpression
	| op = 'return' expression                                  # ContinueBreakReturnExpression
	| '(' expression ')'										# GroupedExpression
	| expressionWithBlock										# ExpressionWithBlock_
	| '['parameter? (',' parameter)*']'                         # ArrayExpression;

parameter: literalExpression | pathExpression ;

expressionWithBlock: loopExpression | ifExpression;

loopExpression: infiniteLoopExpression | predicateLoopExpression;

infiniteLoopExpression: 'loop' blockExpression;

predicateLoopExpression: 'while' expression blockExpression;

ifExpression: 'if' expression blockExpression ('else' (blockExpression | ifExpression))?;

pathExpression: '::'? identificator ('::' identificator)*;

literalExpression: INT | STRING | KW_TRUE | KW_FALSE;

comparisonOperator: EQEQ | NE | GT | LT | GE | LE;

identificator: ID;

compoundAssignOperator:
	PLUSEQ
	| MINUSEQ
	| STAREQ
	| SLASHEQ
	| PERCENTEQ
	| ANDEQ
	| OREQ
	| CARETEQ
	| SHLEQ
	| SHREQ;

KW_AS: 'as';
KW_BREAK: 'break';
KW_CONST: 'const';
KW_CONTINUE: 'continue';
KW_CRATE: 'crate';
KW_ELSE: 'else';
KW_ENUM: 'enum';
KW_EXTERN: 'extern';
KW_FALSE: 'false';
KW_FN: 'fn';
KW_FOR: 'for';
KW_IF: 'if';
KW_IMPL: 'impl';
KW_IN: 'in';
KW_LET: 'let';
KW_LOOP: 'loop';
KW_MATCH: 'match';
KW_MOD: 'mod';
KW_MOVE: 'move';
KW_MUT: 'mut';
KW_PUB: 'pub';
KW_REF: 'ref';
KW_RETURN: 'return';
KW_SELFVALUE: 'self';
KW_SELFTYPE: 'Self';
KW_STATIC: 'static';
KW_STRUCT: 'struct';
KW_SUPER: 'super';
KW_TRAIT: 'trait';
KW_TRUE: 'true';
KW_TYPE: 'type';
KW_UNSAFE: 'unsafe';
KW_USE: 'use';
KW_WHERE: 'where';
KW_WHILE: 'while';
KW_ASYNC: 'async';
KW_AWAIT: 'await';
KW_DYN: 'dyn';
KW_ABSTRACT: 'abstract';
KW_BECOME: 'become';
KW_BOX: 'box';
KW_DO: 'do';
KW_FINAL: 'final';
KW_MACRO: 'macro';
KW_OVERRIDE: 'override';
KW_PRIV: 'priv';
KW_TYPEOF: 'typeof';
KW_UNSIZED: 'unsized';
KW_VIRTUAL: 'virtual';
KW_YIELD: 'yield';
KW_TRY: 'try';


INT_SUFFIX: [i] ('8' | '16' | '32' | '64' | 'size');

ID: [a-zA-Z_][a-zA-Z0-9_]* [!]?;

INT:
	[0-9]+ INT_SUFFIX?
	| '0x' '_'* [0-9a-fA-F] [0-9a-fA-F_]* INT_SUFFIX?
	| '0o' '_'* [0-7] [0-7_]* INT_SUFFIX?
	| '0b' '_'* [01] [01_]* INT_SUFFIX?;


STRING: '"' ~([\n\r]|'"')* '"';

PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
PERCENT: '%';
CARET: '^';
NOT: '!';
AND: '&';
OR: '|';
ANDAND: '&&';
OROR: '||';
PLUSEQ: '+=';
MINUSEQ: '-=';
STAREQ: '*=';
SLASHEQ: '/=';
PERCENTEQ: '%=';
CARETEQ: '^=';
ANDEQ: '&=';
OREQ: '|=';
SHLEQ: '<<=';
SHREQ: '>>=';
EQ: '=';
EQEQ: '==';
NE: '!=';
GT: '>';
LT: '<';
GE: '>=';
LE: '<=';
AT: '@';
UNDERSCORE: '_';
DOT: '.';
DOTDOT: '..';
DOTDOTDOT: '...';
DOTDOTEQ: '..=';
COMMA: ',';
SEMI: ';';
COLON: ':';
PATHSEP: '::';
RARROW: '->';
FATARROW: '=>';
POUND: '#';
DOLLAR: '$';
QUESTION: '?';
LCURLYBRACE: '{';
RCURLYBRACE: '}';
LSQUAREBRACKET: '[';
RSQUAREBRACKET: ']';
LPAREN: '(';
RPAREN: ')';

WS: [ \n\t\r]+ -> skip;
LINECOMMENT: '//' ~[\r\n]* -> skip;
BLOCKCOMMENT: '/*' ~[\r\n]* '*/' -> skip;