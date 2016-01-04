package model;

import java.util.Map;

public class TestBuildSection extends BasicSection{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7431817247915057285L;
	private TestBuild testBuild = new TestBuild();
	
	public TestBuildSection() {
		super();
		this.setName(KeyWords.TEST_BUILD_PHASENAME.toString());
		// TODO Auto-generated constructor stub
	}
	
	public TestBuildSection(String variable) {
		super();
		this.setName(KeyWords.TEST_BUILD_PHASENAME.toString());
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @return the testBuild
	 */
	public TestBuild getTestBuild() {
		return testBuild;
	}

	/**
	 * @param testBuild the testBuild to set
	 */
	public void setTestBuild(TestBuild testBuild) {
		this.testBuild = testBuild;
	}

	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		String phaseName = KeyWords.TEST_GET_PHASENAME.toString();
		String phaseNameAsKey = KeyWords.TEST_GET_KEY.toString();

		//If "name-section" map is empty, which means there's no valid section in the file
		boolean regular_check_result = true;
		boolean result = true;

		try {
			regular_check_result = regular_check(sectionMap);
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (regular_check_result) {
			try {
				result = specific_key_check_and_add_relations(sectionMap, phaseName, phaseNameAsKey);
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				
			}
		}else{
			return false;
		}
		return result;
	}
}
