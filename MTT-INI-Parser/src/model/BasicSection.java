package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public abstract class BasicSection extends ExecutionNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5263969634986813877L;
	private String name = "";
	private KeyValuePair<String, String> keyValuePairs = new BaseKeyValuePair<String, String>();
	public abstract boolean semanticCheck(Map<String, BasicSection> sectionMap);
	private String variable = "";
	private BasicSection includingSection = null;
	private boolean resolved = false;
	private List<BasicSection> relatedSections = new ArrayList<BasicSection>();
	
	private Map<String, Integer> execEnumerateMap = new HashMap<String, Integer>();
	
	
	/**
	 * @return the execEnumerateMap
	 */
	public Map<String, Integer> getExecEnumerateMap() {
		return execEnumerateMap;
	}

	/**
	 * @param execEnumerateMap the execEnumerateMap to set
	 */
	public void setExecEnumerateMap(Map<String, Integer> execEnumerateMap) {
		this.execEnumerateMap = execEnumerateMap;
	}

	/**
	 * @return the relatedSections
	 */
	public List<BasicSection> getRelatedSections() {
		return relatedSections;
	}

	/**
	 * @param relatedSections the relatedSections to set
	 */
	public void setRelatedSections(List<BasicSection> relatedSections) {
		this.relatedSections = relatedSections;
	}

	protected BasicSection getIncludingSection() {
		return includingSection;
	}

	protected void setIncludingSection(BasicSection includingSection) {
		this.includingSection = includingSection;
	}

	protected boolean isResolved() {
		return resolved;
	}

	protected void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	protected String getFullName() {
		if(this.variable.equals("")){
			return this.name;
		}else{
			return this.name+":"+this.variable;
		}
	}
	
	/**
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public BasicSection() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BasicSection(String name) {
		super();
		this.setName(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public KeyValuePair<String, String> getKeyValuePairs() {
		return keyValuePairs;
	}

	public void setKeyValuePairs(KeyValuePair<String, String> keyValuePairs) {
		this.keyValuePairs = keyValuePairs;
	}
	
	protected String printKVP() {
		int n = this.getKeyValuePairs().size();
		int i = 0;
		for(Entry<String, String> entry: this.getKeyValuePairs().entrySet()){
			System.out.println("    "+i+") "+entry.getKey()+" = "+entry.getValue());
			i++;
		}
		System.out.println("Section "+this.getFullName()+" has "+n+" kvps in total.");
		return "";
	}
	
	/**
	 * 
	 * @param customKVP
	 */
	protected void overrideKVP(KeyValuePair<String, String> customKVP){
		this.getKeyValuePairs().putAll(customKVP);
	}
	
	/**
	 * 
	 * @return
	 */
	protected KeyValuePair<String, String> getKeyValuePairExceptIncludeSectionOne() {
		KeyValuePair<String, String> keyValuePairExceptIncludeSectionKeyValuePair = this.getKeyValuePairs();
		String include_section_keyString = KeyWords.INCLUDE_SECTION.toString();
		if(keyValuePairExceptIncludeSectionKeyValuePair.containsKey(include_section_keyString)){
			keyValuePairExceptIncludeSectionKeyValuePair.remove(include_section_keyString);
			return keyValuePairExceptIncludeSectionKeyValuePair;
		}
		else{
			return keyValuePairExceptIncludeSectionKeyValuePair;	
		}
	}
	
	/**
	 * Check whether section map is empty or not
	 * @param sectionMap
	 * @return
	 * @throws SemanticException 
	 */
	protected boolean regular_check(Map<String, BasicSection> sectionMap) throws SemanticException{
		String thisSectionName = this.getName()+":"+this.getVariable();

		boolean regular_check_result = true;
		//If "name-section" map is empty, which means there's no valid section in the file
		if(sectionMap.isEmpty()){
			throw new SemanticException("Semantic error 4: section map is empty.", true);
		}
		
		
		//return true;
		
		//If the key-value pairs in this section is empty, which means there's no valid k-v pair in this section.
		if(this.getKeyValuePairs().isEmpty()){
			regular_check_result = false;
			throw new SemanticException("Semantic error 3: No key-value pairs in "+
					thisSectionName+" section.", false);
		}
		return regular_check_result;
	}

	/**
	 * 
	 * @param sectionMap
	 * @param phaseName
	 * @param phaseNameAsKey
	 * @return
	 * @throws SemanticException
	 */
	protected boolean specific_key_check_and_add_relations(Map<String, BasicSection> sectionMap,
			String phaseName, String phaseNameAsKey) throws SemanticException{
		boolean specific_key_check_result = true;
		String thisSectionName = this.getName()+":"+this.getVariable();
		
		//If this section's k-v pair contains one which key is "mpi_get"
		if(this.getKeyValuePairs().containsKey(phaseNameAsKey)){
			//Get all of the values corresponding to "mpi_get"
			String variable = this.getKeyValuePairs().get(phaseNameAsKey);
			//If the value is keyword "all"
			if(variable.equals(KeyWords.ALL_PHASENAME.toString())){
				for(String sectionMapKey: sectionMap.keySet()){
					if(sectionMapKey.startsWith(phaseName)){
						this.relatedSections.add(sectionMap.get(sectionMapKey));
					}
				}
			}else{
				String[] variables = variable.split(",");
				//Preparing a empty key list for comparing keys with "name-section" map
				List<String> keys = new ArrayList<String>();
				//Generate the key in format of "phase name:value"
				//i.e. key = "MPI get:nightly v1.8"
				for(int i = 0; i < variables.length; i++){
					keys.add(i, phaseName+":"+variables[i]);
				}
				
				//For each generated key, finding the corresponding key in "name-section" map.
				//Notice that the "containsKey" method might not work here according to hash code.
				
				for(String key : keys){
					for(String sectionMapKey : sectionMap.keySet()){
						if(sectionMapKey.equals(key)){
							specific_key_check_result = true;
							//add this section to relationship collection.
							this.relatedSections.add(sectionMap.get(key));
							break;
						}
						else {
							specific_key_check_result = false;
						}
					}
					if(!specific_key_check_result){
						throw new SemanticException
						("Semantic error : Section: "+key+" is missing according to KVP: "+phaseNameAsKey+":"+variable
						+" in "+thisSectionName+" section", false);
					}
				}
			}	
		}
		else{
			throw new SemanticException
			("Semantic error : Key: "+phaseNameAsKey+" is missing in "+thisSectionName+" section", false);
		}
		return specific_key_check_result; 
 
	}
	
}
