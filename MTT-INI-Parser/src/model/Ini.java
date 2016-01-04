package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Ini {
	private Map<String, BasicSection> section_name_map = new ConcurrentHashMap<String, BasicSection>();
	private List<ExecutionNode> MPIGetRootList;
	public List<ExecutionNode> getMPIGetRootList() {
		return MPIGetRootList;
	}

	public void setMPIGetRootList(List<ExecutionNode> mPIGetRootList) {
		MPIGetRootList = mPIGetRootList;
	}
	
	public void addNameObjectPairsToSectionNameMap(String key, BasicSection value){
		this.section_name_map.put(key, value);
	}
	
	public Map<String, BasicSection> getSection_name_map() {
		return section_name_map;
	}

	public void setSection_name_map(Map<String, BasicSection> section_name_map) {
		this.section_name_map = section_name_map;
	}

	public Map<String, Boolean> getSectionNumberCheckMap() {
		return sectionNumberCheckMap;
	}

	public void setSectionNumberCheckMap(Map<String, Boolean> sectionNumberCheckMap) {
		this.sectionNumberCheckMap = sectionNumberCheckMap;
	}

	private Map<String, Boolean> sectionNumberCheckMap = new HashMap<String, Boolean>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = -1374245243982504409L;

		/**
		 * 
		 */
		{	
			put("MTT",false);
			put("MPI get", false);
			put("MPI install", false);
			put("MPI Details", false);
			put("Test get", false);
			put("Test build", false);
			put("Test run", false);
			put("Reporter", false);
		}
	}; 
	
	//Recording section name in another smaller collection therefore we
	// can notice the section missing easier.
	public void recordSectionName(String sectionNameString){
		this.getSectionNumberCheckMap().replace(sectionNameString, true);
	}
	
}
