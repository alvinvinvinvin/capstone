package model;

import java.util.Map;

public class MPIInstallSection extends BasicSection {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7030546429201903343L;
	private Install install = new Install();
	
	public MPIInstallSection() {
		super();
		this.setName(KeyWords.MPI_INSTALL_PHASENAME.toString());
		// TODO Auto-generated constructor stub
	}
	public MPIInstallSection(String variable) {
		super();
		this.setName(KeyWords.MPI_INSTALL_PHASENAME.toString());
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @return the install
	 */
	public Install getInstall() {
		return install;
	}
	/**
	 * @param install the install to set
	 */
	public void setInstall(Install install) {
		this.install = install;
	}
	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		
		String phaseName = KeyWords.MPI_GET_PHASENAME.toString();
		String phaseNameAsKey = KeyWords.MPI_GET_KEY.toString();

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