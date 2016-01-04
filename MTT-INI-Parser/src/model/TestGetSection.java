package model;

import java.util.Map;

public class TestGetSection extends BasicSection {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4532507310280967972L;

	public TestGetSection() {
		super();
		this.setName(KeyWords.TEST_GET_PHASENAME.toString());
		// TODO Auto-generated constructor stub
	}
	public TestGetSection(String variable) {
		super();
		this.setName(KeyWords.TEST_GET_PHASENAME.toString());
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		// TODO Auto-generated method stub
		return true;
	}	
}
