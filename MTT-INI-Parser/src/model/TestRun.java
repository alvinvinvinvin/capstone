package model;

import java.io.Serializable;

/**
 * Test class is child under TestRunSection.
 * @author alvin
 *
 */
public class TestRun implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5272411623914158931L;
	private String testName = "";
	private String avgRunTime = "";
	
	public TestRun(String testName, String avgRunTime){
		this.testName = testName;
		this.avgRunTime = avgRunTime;
	}
	
	/**
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}
	/**
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}
	/**
	 * @return the avgRunTime
	 */
	public String getAvgRunTime() {
		return avgRunTime;
	}
	/**
	 * @param avgRunTime the avgRunTime to set
	 */
	public void setAvgRunTime(String avgRunTime) {
		this.avgRunTime = avgRunTime;
	}

	
	
}
