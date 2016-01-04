package model;


import java.util.Map;

public class MTTSection extends BasicSection{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4667546380151836446L;

	public MTTSection() {
		super();
		this.setName(KeyWords.MTT_PHASENAME.toString());
		this.setVariable("");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean semanticCheck(Map<String, BasicSection> sectionMap) {
		// TODO Auto-generated method stub
		return true;
	}


	
	
}
