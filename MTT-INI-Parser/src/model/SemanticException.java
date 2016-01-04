package model;

public class SemanticException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2829036521344751091L;

	public SemanticException(String sectionNameString, String msg){
		System.err.println("Semantic Error: " +"\n"+
				"Section: "+sectionNameString+"\n"+
				"Error: "+msg);
	}
	
	public SemanticException(String msg, boolean exits){
		System.err.println(msg);
		if(exits){
			System.exit(0);
		}
	}
}
