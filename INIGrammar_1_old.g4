/**
 * Define a grammar called Hello 
 */
grammar INIGrammar_1;
init: (section|NEWLINE)* ;

section:  section_name (contents)+ ; 
//
//
section_name: '[' phase_name ':' v ']'
			  |'[' phase_name ']';
//
phase_name : mtt
			|mpi_get
			|mpi_install
			|mpi_details
			|test_get
			|test_build
			|test_run
			|reporter
			|STRING
			; 
v  : STRING ;      
contents: 
		  kvpairs
		  | NEWLINE
		  ;
kvpairs: keylhs '=' valuerhs (NEWLINE)*
		| keylhs '=' NEWLINE
		;
keylhs : STRING ':' STRING
		|STRING 
		;
valuerhs : eot_section
		|string_with_colon
		|funclet
		|multiline_valuerhs
 		|kvpairs
  		|url 		
  		|STRING
  		;

multiline_valuerhs:	string_with_colon ((',')? (' ')*? ( '\\' (' ')*? NEWLINE)? STRING)+
					|string_with_colon ((',')? (' ')*? ( '\\' (' ')*? NEWLINE)? string_with_colon)+
					|STRING ((',')? (' ')*? ( '\\' (' ')*? NEWLINE)? STRING)+ 
					;
url:STRING '://' STRING;

// Funclet Rules
funclet: |funclet_without_parameters
			|funclet_path
			|funclet_regular
			
			;
funclet_regular: funclet_name '(' (funclet_parameters ((',')? (' ')*? funclet_parameters)*?)*? ')';
funclet_without_parameters: funclet_name '(' ')' ;
funclet_path: funclet_regular STRING ;
funclet_parameters: funclet_without_parameters
								|STRING;
funclet_name:	 |' '*? CONJUNCTION STRING
						|' '*? CONJUNCTION string_with_colon					
						;

//Keywords
mtt: 'MTT';
mpi_get: 'MPI get';
mpi_install:'MPI install';
mpi_details:'MPI Details';
test_get:'Test get';
test_build: 'Test build';
test_run: 'Test run';
reporter: 'Reporter';
eot_section: eot_starter (eot_content)* eot_ender;
eot_starter: EOT_STARTER;
eot_ender: 'EOT';
EOT_STARTER: ' '*?'<<EOT';


eot_content: NEWLINE
			|STRING
			|special_symbol
			;
					 
// Basic Definitions
special_symbol: '='|';'|'!'|':';
string_with_colon: STRING ((':')+ STRING)+;
//funclet_name:CHARS|FUNCLET_NAME;
//charaaa: CHARS;
SEMICOLON: ';';

STRING : (WORDS|SYMBOLS)+ ;

WORDS: (CHARS|NUMBERS)+;
CHARS: [a-zA-Z]+;
NUMBERS: (DIGITS|FLOATS)+;
DIGITS:[0-9]+;
FLOATS: [-+]?[0-9]*.?[0-9]+;
SYMBOLS:['| |.|\-|_|#|"|/|@|<|>|$|%|+|^]+;
//SYMBOLS:['| |.|\-|_|(|)|#|"|/|@|<|>|$|%|+|^]+;
UNDERSCORE: ['_']+;
CONJUNCTION: '&';


//match lower-case identifiers
NEWLINE: ('\r' ? '\n')+ ;
WS : [\t\s\w]+ -> skip ; // skip spaces, tabs, newlines
COMMENT: '#' ~[\r\n]* (EOF|'\r'? '\n')  -> skip;
EMPTY: NEWLINE -> skip;
EMPTYLINE: '\r\n' -> skip;
