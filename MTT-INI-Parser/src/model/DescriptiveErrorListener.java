package model;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public  class DescriptiveErrorListener extends BaseErrorListener {
	public static final DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();
	public DescriptiveErrorListener(){}

	public static int COUNT = 0;
	 
	@Override
    public void syntaxError(Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        COUNT++;
        //System.err.println("rule stack: " + stack);
        System.err.println("Syntax error occurs : line " + line + " : " + 
            charPositionInLine + " at" + offendingSymbol.toString() + ": " + msg);
//        System.exit(0);
    }
}
