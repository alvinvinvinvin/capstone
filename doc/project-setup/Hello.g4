/**
 * Define a grammar called Hello 
 */
grammar Hello;
init :  r (',' r)+ | r (';' r)+ ;

r  : 'hello' ID ;         // match keyword hello followed by an identifier

ID : [a-z]+ | [A-Z]+ | [0-9]+;             // match lower-case identifiers

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

