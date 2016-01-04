/***
 * Excerpted from "The Definitive ANTLR 4 Reference",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpantlr2 for more book information.
***/
// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Test {
    public static void main(String[] args) throws Exception {
    	String strValue;
    	
    	//strValue = "hello world, hello jerry";
    	strValue = "hello world; hello jerry";
    	
        // create a CharStream that reads from standard input
    	//ANTLRInputStream input = new ANTLRInputStream(System.in);
        ANTLRInputStream input = new ANTLRInputStream( strValue );

        // create a lexer that feeds off of input CharStream
        HelloLexer lexer = new HelloLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        HelloParser parser = new HelloParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        System.out.println("");
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
