package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MPIGetSection extends BasicSection {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2356109875026403148L;
	
	private List<Map<String, Integer>> allMpiDetailsExecEnumerateNameAndNum = new ArrayList<Map<String,Integer>>();

	public MPIGetSection() {
		super();
		this.setName(KeyWords.MPI_GET_PHASENAME.toString());
		// TODO Auto-generated constructor stub
	}

	public MPIGetSection(String variable) {
		super();
		this.setName(KeyWords.MPI_GET_PHASENAME.toString());
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @return the allMpiDetailsExecEnumerateNameAndNum
	 */
	public List<Map<String, Integer>> getAllMpiDetailsExecEnumerateNameAndNum() {
		return allMpiDetailsExecEnumerateNameAndNum;
	}

	/**
	 * @param allMpiDetailsExecEnumerateNameAndNum the allMpiDetailsExecEnumerateNameAndNum to set
	 */
	public void setAllMpiDetailsExecEnumerateNameAndNum(
			List<Map<String, Integer>> allMpiDetailsExecEnumerateNameAndNum) {
		this.allMpiDetailsExecEnumerateNameAndNum = allMpiDetailsExecEnumerateNameAndNum;
	}

	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		
		String phaseName = KeyWords.MPI_DETAILS_PHASENAME.toString();
		String phaseNameAsKey = KeyWords.MPI_DETAILS_KEY.toString();

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
