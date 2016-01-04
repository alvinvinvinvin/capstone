package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestRunSection extends BasicSection {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2211465620889253126L;
	private List<TestRun> testRuns = new ArrayList<TestRun>();
	
	public TestRunSection() {
		super();
		this.setName(KeyWords.TEST_RUN_PHASENAME.toString());
		// TODO Auto-generated constructor stub
	}
	public TestRunSection(String variable) {
		super();
		this.setName(KeyWords.TEST_RUN_PHASENAME.toString());
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @return the testRuns
	 */
	public List<TestRun> getTestRuns() {
		return testRuns;
	}
	/**
	 * @param testRuns the testRuns to set
	 */
	public void setTestRuns(List<TestRun> testRuns) {
		this.testRuns = testRuns;
	}
	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {

		String phaseName = KeyWords.TEST_BUILD_PHASENAME.toString();
		String phaseNameAsKey = KeyWords.TEST_BUILD_KEY.toString();
		
		boolean regular_check_result = true;
		boolean result = true;
		//If "name-section" map is empty, which means there's no valid section in the file
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
