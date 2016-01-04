package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestBuild implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 901339439726575540L;
	private String test_suite_name = "";
	private List<TestRun> testRuns = new ArrayList<TestRun>(); 
	private String avgRunTime = "";
	
	public TestBuild(){}
	
	public TestBuild(String test_suite_name, String avgRunTime){
		this.test_suite_name = test_suite_name;
		this.avgRunTime = avgRunTime;
	}
	
	/**
	 * @return the test_suite_name
	 */
	public String getTest_suite_name() {
		return test_suite_name;
	}
	/**
	 * @param test_suite_name the test_suite_name to set
	 */
	public void setTest_suite_name(String test_suite_name) {
		this.test_suite_name = test_suite_name;
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
