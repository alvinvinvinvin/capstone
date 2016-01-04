package model;

import java.util.Map;

public class CustomizedSection extends BasicSection {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1241224567405714661L;
	public CustomizedSection() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomizedSection(String name) {
		super();
		this.setName(name);
		this.setVariable("");
		// TODO Auto-generated constructor stub
	}
	
	public CustomizedSection(String name, String variable){
		super();
		this.setName(name);
		this.setVariable(variable);
	}
	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
