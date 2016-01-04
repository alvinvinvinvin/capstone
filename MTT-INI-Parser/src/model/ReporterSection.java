package model;


import java.util.Map;

public class ReporterSection extends BasicSection {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7430283174049050267L;

	public ReporterSection() {
		super();
		this.setName("Reporter");
		// TODO Auto-generated constructor stub
	}
	public ReporterSection(String variable) {
		super();
		this.setName("Reporter");
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
