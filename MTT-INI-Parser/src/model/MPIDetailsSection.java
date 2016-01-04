package model;

import java.util.Map;

public class MPIDetailsSection extends BasicSection {
	/**
	 * 
	 */
	private static final long serialVersionUID = 563687965367648534L;

	public MPIDetailsSection() {
		super();
		this.setName(KeyWords.MPI_DETAILS_PHASENAME.toString());
		// TODO Auto-generated constructor stub
	}
	
	public MPIDetailsSection(String variable) {
		super();
		this.setName(KeyWords.MPI_DETAILS_PHASENAME.toString());
		this.setVariable(variable);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		return true;
	}

}
