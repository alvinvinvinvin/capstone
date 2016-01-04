package model;


import generatedCode.INIGrammar_1BaseVisitor;
import generatedCode.INIGrammar_1Parser;

import java.util.ArrayList;
import java.util.List;

import model.BasicSection;
import model.Convertor;
import model.Ini;



public class NodeVisitor extends INIGrammar_1BaseVisitor<String>{
	
	private Ini myIni = new Ini();
	private Convertor convertor = new Convertor();
	private List<BasicSection> basicSections = new ArrayList<BasicSection>();

	/**
	 * It has to return a string value but what we need is a list of sections.
	 * Therefore we have to return a empty value string and store
	 * the list to the local field "basicSections" list.
	 * And we have already stored section name-object map to myIni object
	 * by calling convertSyntaxTreeToObjects.
	 */
	@Override
	public String visitSection( INIGrammar_1Parser.SectionContext ctx){
		super.visitSection(ctx);
		try {
			this.basicSections.add(convertor.convertSyntaxTreeToObjects(myIni, ctx));
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		//return visitChildren(ctx);
	}


	public Ini getMyIni() {
		return myIni;
	}


	public void setMyIni(Ini myIni) {
		this.myIni = myIni;
	}

	public List<BasicSection> getBasicSections() {
		return basicSections;
	}


	public void setBasicSections(List<BasicSection> basicSections) {
		this.basicSections = basicSections;
	}
	
	
	
}
