package controller;

/***
 * Excerpted from "The Definitive ANTLR 4 Reference",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpantlr2 for more book information.
***/
// import ANTLR's runtime libraries
import generatedCode.INIGrammar_1Lexer;
import generatedCode.INIGrammar_1Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

import model.BasicSection;
import model.CommonHelper;
import model.Convertor;
import model.DescriptiveErrorListener;
import model.ExecutionNode;
import model.Ini;
import model.NodeVisitor;
import model.SemanticException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

//import com.sun.xml.internal.bind.api.ErrorListener;

public class MCFA {
	
    public static void main(String[] args) throws Throwable {
    	fileValidation(args);
    }
    
    /**
     * 
     * @param args
     * @throws Throwable 
     * @throws Exception
     */
    private static void fileValidation(String[] args) throws Throwable{
    	int numOfargs = args.length;
    	String filePathString = "";
    	if(numOfargs == 0){
    		System.out.println("Need INI file path.");
    		System.out.println("Example: \"java -jar mcfa.0.1.jar <INI file path>\"");
    		System.exit(0);
    	}else if(numOfargs == 1) {
			filePathString = args[0];
		}else{
			System.out.println("Need valid INI file path.");
    		System.out.println("Example: \"java -jar mcfa.0.1.jar <INI file path>\"");
    		System.exit(0);
		}
    	PathMatcher pathMatcher =  FileSystems.getDefault().getPathMatcher("glob:**.{ini, INI, Ini}");
    	Path filePath = Paths.get(filePathString);
    	if(pathMatcher.matches(filePath)){
    		System.out.println("Reading file path....");
        	String everything = fileReader(filePathString);
        	System.out.println("Checking syntax....");
            errorListenerTest(everything);
    	}
    	else{
    		System.out.println("Need a valid INI file. File "+"\""+filePathString+"\""+" has invalid extension.");
    		System.out.println("Example: \"java -jar mcfa.0.1.jar example.ini\"");
    		System.exit(0);
    	}
    }
    
    /**
     * 
     * @param filePathString
     * @return
     * @throws Exception
     */
    private static String fileReader(String filePathString){
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePathString));
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.exit(0);
		}
    	String everything="";
        try {
            StringBuilder sb = new StringBuilder();
            String line = null;
			try {
				System.out.println("Reading INI file...");
				line = br.readLine();
			} catch (IOException e) {

				e.printStackTrace();
			}

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                try {
					line = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            everything = sb.toString();
        } finally {
            try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return everything;
    }
    
    /**
     * 
     * @param everything
     * @throws Throwable 
     * @throws Exception
     */
    public static void errorListenerTest(String everything) throws Throwable {

    	/**
    	 * 1. 1. syntax tree generation
    	 * */
    	DescriptiveErrorListener dErrorListener = DescriptiveErrorListener.INSTANCE;
        // create a CharStream that reads from standard input
    	ANTLRInputStream input = new ANTLRInputStream( everything );

        // create a lexer that feeds off of input CharStream
        INIGrammar_1Lexer lexer = new INIGrammar_1Lexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(dErrorListener);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        INIGrammar_1Parser parser = new INIGrammar_1Parser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(dErrorListener);
        
        
        
        ParseTree tree = parser.r(); // begin parsing at init rule
        
        //Count syntax error number.
        int syntaxNum = DescriptiveErrorListener.COUNT;
        if(syntaxNum > 0){
        	System.err.println("\n"+"There are "+syntaxNum+" syntax errors shown above, please correct them.");
        	System.exit(0);
        }
        
        System.out.println("Checking syntax complete. No error.");
        System.out.println("Trying to check semantic errors....");
        /*Syntax tree generated. */
        
        /**
         * 2. convert syntax tree to object collection (map)
         * 
         */
        /*Start walk through syntax tree*/
        NodeVisitor nv = new NodeVisitor();
        
        //Here, NodeVisitor will walk through the ParseTree, and converting
        //tree node to object therefore to instantiate a Ini object.
        /**
         * In "visit" method, it will embed functions to convert syntax tree to object collection.
         */
        nv.visit(tree);
        
        /**
         * Converting done.
         */
        Ini myIni = nv.getMyIni();
        CommonHelper chHelper = new CommonHelper();
        try {
        	
        	/*
        	 * Important!:
        	 * 1) Solve "include_section" first, this is the first thing must be done after syntax tree built.
        	 * 2) check default KVPs in default sections.
        	 * 3) Solve "@" quotations.
        	 * 3) build object tree.
        	 */
			chHelper.setupSections(myIni);
		}catch(SemanticException se){
			se.getStackTrace();
		}catch (Exception e) {
			e.printStackTrace();

		}
        /**
         * 3. do the replacing, reference stuff
         */
        
        /**
         * 4. semantic check
         */
        
        /**
         * 5. convert object collection to execution tree
         */
        
        /**
         *  6. print execution tree
         */
        //Initialize an operation class to do more operations
        System.out.println("Checking default Sections existence...");
        chHelper.sectionMissingCheckAndPrint(myIni);
        System.out.println("Default Sections existence checking complete.");
        
        //Check defualt KVPs in each section.
        System.out.println("Checking default KVP existence...");
        boolean defaultKVPExistence = true;
        for(BasicSection bs: myIni.getSection_name_map().values()){
        	defaultKVPExistence = bs.semanticCheck(myIni.getSection_name_map());
        }

        if(defaultKVPExistence){
        	System.out.println("Default KVP existence checking complete.");
        }else{
        	throw new SemanticException("Default KVP existence checking found errors.", true);
        }
        
        //Print the section_name_head and section_name_value map
//        System.out.println("printing section maps:");
//        chHelper.printSectionNameMap(myIni);

        List<BasicSection> sections = nv.getBasicSections();
        int sectionSize = sections.size();

        System.out.println("section size is: "+sectionSize);
        
        Convertor convertor = new Convertor();
        System.out.println("Constructing execution tree...");
        System.out.println("Trying to request historical runtime data from server...");
        ExecutionNode root = convertor.buildExecutionTree(myIni.getSection_name_map());
        System.out.println("Execution tree contsruction complete.");

        
        chHelper.displayTree(root, 0);
//        DatabaseConnector dConnector = new DatabaseConnector();
//        try {
////			dConnector.testSuiteRequest("trivial");
////			System.out.println(dConnector.test_runRuntimeRequest("mca"));
//			System.out.println(dConnector.test_buildRuntimeRequest("map"));
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        //ExecutionNode root = convertor.convertSyntaxTreeToExecutionTree(nv.getMyIni().getSection_name_map());
        //System.out.println(chHelper.printExecutionTree(root));
    }
}
