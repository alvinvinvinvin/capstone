/**
 * Define a grammar called Hello
 * 
 * WARNING:
 * 
 * The whole program is tightly relying on every single rule structure
 * of this grammar file. Any slightly change of this file will cause unexpected
 * huge affects to the whole program. Please beware of it.
 */
grammar INIGrammar_1;

@header{
	package generatedCode;
}

r  : file EOF;

file: (section|NEWLINE)*;

/**
 * Section rules
 * 
 * Sections are the basic elements of INI file. 
 * It contains section name and section body.
 */
section
	: section_name NEWLINE+ section_body?;
/**
 * Section name
 */
section_name
	:WHITESPACES? LEFTSQUAREBRACKET section_name_head ( COLON  section_name_variable )?  RIGHTSQUAREBRACKET WHITESPACES?;
//Does section name allow numbers?
section_name_head
	:WHITESPACES? STRING (WHITESPACES STRING)* WHITESPACES?;
section_name_variable
	:WHITESPACES? STRING (WHITESPACES STRING)* WHITESPACES?;
/**
 * Section body
 * 
 * Basically section body is consisted by Key-Value pairs
 */

section_body
	: (kv_pair NEWLINE)+;

/**
 * Key-Value pair
 */

kv_pair
	:  key  EQUALSIGN  vrhs? ;
doublequotation_kv_pair
	:DOUBLEQUOTATION kv_pair DOUBLEQUOTATION;

/**
 * Key
 * 
 * The left hand side element of Key-Value pair
 */
key
	: key_head  COLON  key_parameter 
	| key_head 
	;
key_head
	:WHITESPACES? STRING WHITESPACES?;
key_parameter
	:WHITESPACES? STRING WHITESPACES?;
/**
 * Value
 * 
 * The right hand side element of Key-Value pair
 */
vrhs
	:WHITESPACES? vrhs_content (WHITESPACES vrhs_content )* WHITESPACES?
	|WHITESPACES? plain_text_with_dollar_quotation WHITESPACES?;
vrhs_content
			:eot_section
			|path
			|funclet_without_parameter
			|funclet_regular 
			|http_link
			|kv_pair
			|doublequotation_kv_pair
//			| command_line
//			| multi_command_line
			| quoted_string
			| quotation
			| comma_split_string
			| STRING
			| DOT
			| dollar_quotation
			|email
			;
/**
 * plain text
 */
 plain_text_with_dollar_quotation
 	: (plain_text|dollar_quotation)+;
 plain_text
 	: (STRING|WHITESPACES|COLON|BACKWARD_SLASH)+;
/**
 * Dollar symbol quotation
 */
dollar_quotation
	: DOLLAR STRING;

/**
 * Quoted string
 */
quoted_string
	: DOUBLEQUOTATION (STRING|WHITESPACES|COMMA|DOT)* DOUBLEQUOTATION;

/**
 * Comma split string
 */
 comma_split_string
 	: STRING WHITESPACES? (COMMA WHITESPACES? STRING)+ ;

/**
 * Email
 */
email
	: email_address AT domain_name;
email_address
	: (STRING|DOT)+;
domain_name
	: STRING DOT STRING;
/**
 * Path
 * 
 * Path is file directory we used a lot in regular operating system. In this grammar, path
 * also could contain funclets and their parameters.
 */
 
 path
 	: BACKWARD_SLASH? path_name+  (BACKWARD_SLASH  path_name+)+ (DOT extension)? 
 	| BACKWARD_SLASH? path_name+ DOT extension
 	| BACKWARD_SLASH? path_name+ BACKWARD_SLASH
 	| BACKWARD_SLASH
 	;
 path_name
 			:normal_path_name
 			|funclet_regular
 			|funclet_without_parameter
 			|dollar_quotation;
 normal_path_name
 	: STRING (WHITESPACES STRING)*;
 extension
 	:STRING;

/**
 * http link
 */

http_link
	:HTTP_LINK;

/**
 * Quotation
 */
quotation
	: AT quotation_name AT ;
quotation_name
	: STRING;

/**
 * Command line
 */
//multi_command_line: command_line+ ;
//
//command_line: MINUS_OR_DOUBLEMINUS command (WHITESPACES (command_following_parameters|command_splited_by_comma))*;
//		
//command_splited_by_comma: command_following_parameters (COMMA command_following_parameters)+;
//command:(STRING|NUMBERS);
//command_following_parameters: (STRING|NUMBERS);


/**
 * Funclet
 * 
 * Funclet is the most special feature of MTT configuration file comparing to
 * regular INI file we see in common. It basically looks like function with parameters.
 * However it includes more special syntax elements than structure of regular
 * functions we see in JAVA.
 * 
 */
funclet_regular
	:CONJUNCTION funclet_name LEFTPARENTHESE WHITESPACES? (funclet_parameters) (WHITESPACES? COMMA WHITESPACES? (funclet_parameters))* WHITESPACES? RIGHTPARENTHESE;
funclet_without_parameter
	:CONJUNCTION funclet_name LEFTPARENTHESE WHITESPACES? RIGHTPARENTHESE ;

funclet_name
			: string_with_doublecolons
			| STRING
			;
funclet_parameters
	: funclet_parameter+;
funclet_parameter
					: funclet_regular math_formula*
					|funclet_without_parameter math_formula*
					|parameter_doublequotation
					|quotation
					|STRING
					|DOT
					|WHITESPACES
					;
parameter_doublequotation
	: DOUBLEQUOTATION WHITESPACES? parameter_content? (WHITESPACES parameter_content)* WHITESPACES? DOUBLEQUOTATION;

parameter_content
					:funclet_without_parameter
					|quotation
					|comma_split_string
					|path
					|STRING
					|DOT
					;

/**
 * EOT section
 */
 eot_section
 	: EOT_STARTER eot_contents EOT_ENDER;
 eot_contents
 	: ~(EOT_STARTER|EOT_ENDER)* ;
 
EOT_STARTER: '<<EOT';
EOT_ENDER: 'EOT';
/**
 * Useful parser rules
 */
string_with_doublecolons
	: STRING (DOUBLECOLONS STRING)+;
math_formula
	:WHITESPACES? MATH_OPERATORS WHITESPACES? STRING;

/**
 * Lexer rules
 * */
/**
 * Character rules
 * 
 */
STRING: (CHAR|UNDERSCORES|REGULARSYMBOLS|NUMBERS|MINUS)+;

NUMBERS: (DIGIT|FLOAT)+;
//HTTP_LINK:HTTP_HEAD ~[\r\n|\n]* (EOF|('\r'? '\n'));
HTTP_LINK:HTTP_HEAD (STRING|DOT)+ (BACKWARD_SLASH (STRING|DOT)+ )* BACKWARD_SLASH?;

fragment HTTP_HEAD: 'http' 's'? '://';
fragment CHAR: [a-zA-Z];
DIGIT:[0-9];
FLOAT: [-+]?[0-9]*'.'?[0-9]+;


/**
 * Symbol rules
 */
REGULARSYMBOLS: ['|<|>|^|+|%|;|!]+;
fragment PLUS:'+';
fragment STAR:'*';
BACKWARD_SLASH:'/';
DOT:'.';
DOLLAR: '$';
fragment UNDERSCORES: [_]+;
fragment SLASH_DOUBLEQUOTATION:'\"';
fragment ESCAPED_DOUBLEQUOTATION:'\\"';

/**
 * Mathematical operators
 */
MATH_OPERATORS:PLUS|MINUS|STAR;

/**
 * Special symbols
 */
MINUS_OR_DOUBLEMINUS: MINUS|DOUBLE_MINUS;
LEFTSQUAREBRACKET:'[';
RIGHTSQUAREBRACKET:']';
MINUS: '-';
DOUBLE_MINUS:'--';
CONJUNCTION: '&';
AT: '@';
DOUBLEQUOTATION: '"';
LEFTPARENTHESE: '(';
RIGHTPARENTHESE: ')';
WHITESPACES: [ |\t]+;
COLON: ':';
DOUBLECOLONS:'::';
EQUALSIGN: '=';
COMMA:',';


/**
 * New line symbol
 */
NEWLINE: ('\r' ? '\n')+ ;


/**
 * Skips
 */
COMMENT: '#' ~[\r\n|\n]* (EOF|NEWLINE)  -> skip;
FORWARDSLASH:'\\' WHITESPACES? NEWLINE WHITESPACES?-> skip;

