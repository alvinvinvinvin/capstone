package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonHelper {
	/**
	 * Constructor
	 */
	public CommonHelper(){}
	
	/**
	 * 
	 * @param myIni
	 * @throws SemanticException
	 */
	public void setupSections(Ini myIni) throws SemanticException{
		/*	
		 * Adding corresponding section objects to "including_section" attribute of each
		 * section object.
		 *
		 */
		setIncludingSections(myIni);
		
		/*
		 * 1) recursively replace "include sections"
		 * 2) replace @mca@
		 * 3) &enumerate
		 */
		if(replaceIncludeSections(myIni)){
			System.out.println("Section Included Key-Value Pairs Replacement Complete.");
		}else{
			throw new SemanticException("Error happened during dealing with include_sections", true);
		}
		
		
		if(replaceAtQuotations(myIni)){
			System.out.println("Section At Quotation Replacement Complete.");
		}else{
			throw new SemanticException("Error happened during dealing with At Quotations", true);
		}
//		System.out.println("123");
//		test(myIni.getSection_name_map());
	}
	

	/**
	 * Print all section map entries out for debugging convenience.
	 * @param ini
	 */
	public void printSectionNameMap(Ini ini){
		int i = 1;
		for(Map.Entry<String, BasicSection> e : ini.getSection_name_map().entrySet()){
			System.out.println(i+": "+e.getKey() + " = " + e.getValue().getVariable());
			i++;
		}
	}
	
	/**
	 * Storing each path of including chain to a map. After reach the tail
	 * of the chain, overriding the kvps in reversed order (from tail to top).
	 * And delete the include_section kvp, updating it to myIni. Therefore
	 * in next round of checking we don't have to deal with updated section
	 * again. Instead of it we just simply take the updated section as the 
	 * tail of next including chain. 
	 * We store each of chain-map to a list, finish overriding task, and 
	 * update them all to myIni.
	 * @param inputIni
	 * @return
	 */
	private boolean replaceIncludeSections(Ini inputIni){
		
		Map<String, BasicSection> includedSectionsMap = new LinkedHashMap<String, BasicSection>();
		
		/*Loop the section map in myIni to find out which section contains
		 * "include_section" kvp*/
		Iterator<BasicSection> myIterator = inputIni.getSection_name_map().values().iterator();

		while(myIterator.hasNext()){
		
			/*recursively build the including chain.
			 * Right after accomplished the building, start
			 * overriding. During overriding, remove the 
			 * "include_section" kvp, put a mark on that section
			 * called "resolved". Therefore next loop round when we
			 * want to build next including chain, if another section
			 * also refers to this section, this section will be the
			 * tail because its already been updated. By this we could
			 * save some time on overriding in following including chains.*/ 
			
			BasicSection basicSection = myIterator.next();
			try {
				includedSectionsMap = buildingIncludingChain(basicSection, includedSectionsMap);

				/*
				 * Update Ini section map immediately therefore next round won't
				 * waste time on resolved section objects.
				 */
				
				if (includedSectionsMap.size()>1) {
					overrideIncludedSections(includedSectionsMap);

					inputIni.getSection_name_map().putAll(includedSectionsMap);

				}
			} catch (SemanticException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
			/*
			 * Dump the old map off. Preparing a new one for next round use.
			 */
			includedSectionsMap = new LinkedHashMap<String, BasicSection>();
		}

		return true;
		
	}
	
	
	/**
	 * Print out the replaced kvps.
	 * @param includedSections
	 */
	@SuppressWarnings("unused")
	private void test(Map<String, BasicSection> includedSections){
		String resolved = "";
		int i = 1;
		for(Map.Entry<String, BasicSection> entry: includedSections.entrySet()){
			if(entry.getValue().isResolved()){
				resolved = "resolved";
			}
			System.out.println(i+": "+entry.getKey()+" "+resolved);
			entry.getValue().printKVP();
			i++;
		}
	}
	
	/**
	 * Depending on the includingSection Attribute, we could recursively build
	 * an including chain for each section object as a map.
	 * @param root
	 * @param includingChainMap
	 */
	private Map<String, BasicSection> buildingIncludingChain(BasicSection root, Map<String, BasicSection> includingChainMap)
	throws SemanticException{
		if (root.getIncludingSection() == null){
			includingChainMap.put(root.getFullName(), root);
			return includingChainMap;
		}else{
			includingChainMap.put(root.getFullName(), root);
			return buildingIncludingChain(root.getIncludingSection(), includingChainMap);
		}
	}
	
	
	/**
	 * Override KVP in sections by reversed order one by one. We only set "resolved" attribute here
	 * right after we overriding the k-v pairs. Therefore other parts won't mess up the relations.
	 * @param inputList
	 */
	private void overrideIncludedSections(Map<String, BasicSection> inputMap){
		List<String> keyStrings = new ArrayList<String>(inputMap.keySet());
		String include_section_keyString = KeyWords.INCLUDE_SECTION.toString();
		int n = keyStrings.size();
		for(int i = n - 1; i > 0; i --){
			String keyStringRight = keyStrings.get(i);
			String keyStringLeft = keyStrings.get(i-1);
			BasicSection tempRight = inputMap.get(keyStringRight);
			BasicSection tempLeft = inputMap.get(keyStringLeft);
			tempLeft.overrideKVP(tempRight.getKeyValuePairs());
			tempLeft.getKeyValuePairs().remove(include_section_keyString);
			tempLeft.setIncludingSection(null);
			tempLeft.setResolved(true);
			inputMap.put(keyStringLeft, tempLeft);
		}
	}
	
	/**
	 * For setting up includ_section attribute for each section after
	 * converting done.
	 * @param myIni
	 * @throws SemanticException
	 */
	private void setIncludingSections(Ini myIni) throws SemanticException{
		String include_section_keyString = KeyWords.INCLUDE_SECTION.toString();
		for(BasicSection basicSection : myIni.getSection_name_map().values()){
			if(basicSection.getKeyValuePairs().containsKey(
					include_section_keyString)){
				String included_section_nameString = 
						basicSection.getKeyValuePairs().get(
						include_section_keyString);
				if(myIni.getSection_name_map().containsKey(
						included_section_nameString)){
					BasicSection includedSection = 
							myIni.getSection_name_map().get(
									included_section_nameString);
					basicSection.setIncludingSection(includedSection);
				}else{
					throw new SemanticException(
							"INI file doesn't contain the section: "
					+included_section_nameString, true);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param inputIni
	 * @return
	 * @throws SemanticException 
	 */
	private boolean replaceAtQuotations(Ini myIni){
		/*
		 * Set regex pattern to parse vrhs.
		 * (prefix)(infix)(suffix) == (@) (anything except @) (@)
		 */
		Pattern pattern = Pattern.compile("(@)([^@]*)(@)");
		
		/*
		 * For each section in Ini
		 */
		for(BasicSection basicSection : myIni.getSection_name_map().values()){
			KeyValuePair<String, String> kvps = basicSection.getKeyValuePairs();
			String key = "";
			/*
			 * For each kvp in this section
			 */
			for(Map.Entry<String, String> entry: kvps.entrySet()){
				key = entry.getKey();
				String input = entry.getValue();
				String valueString = "";
				Matcher matcher = pattern.matcher(input);
				/*
				 * 
				 */
				while(matcher.find()){
					/*
					 * group(2) will get the name between two at symbols.
					 */
					String quotation_nameString = matcher.group(2);
					if(basicSection.getKeyValuePairs().containsKey(quotation_nameString)){
						valueString = basicSection.getKeyValuePairs().get(quotation_nameString);
						
						/*
						 * If this is an "exec" KVP, we have to store the number of parameters
						 * of enumerate funclets corresponding to local KVPs. This is the best
						 * chance to get key and number of total paras at same time.
						 */
						if(key.equals(KeyWords.EXEC_KEY.toString())){
							int totalNumOfEnumPara = dealEnumerations(valueString);
							if(totalNumOfEnumPara == -1){
								
							}else{
								basicSection.getExecEnumerateMap().put(quotation_nameString, totalNumOfEnumPara);
							}
							
						}
					
						input = input.replaceAll("@"+quotation_nameString+"@", valueString);
//						int start = matcher.start()+offset;
//						int end = matcher.end() + offset;
//						output.replace(start, end, valueString);
//						offset -= quotation_nameString.length() + valueString.length() +2 - quotation_nameString.length();
					}else{
						System.err.println
						("Warning: Quotation name "+quotation_nameString+
								" doesn't have corresponding key in section "+basicSection.getFullName());
					}
				}
				
				basicSection.getKeyValuePairs().put(key, input);

			}
		}
		return true;
		
	}
	
	
	
	/**
	 * 
	 * @param inputIni
	 * @return
	 */
	private int dealEnumerations(String valueString){
		String enumerateFuncletMatchPattern = "(&enumerate\\()([^\\)]*)(\\))";
		Pattern pattern = Pattern.compile(enumerateFuncletMatchPattern);
		
		String parametersString = "";
		Matcher matcher = pattern.matcher(valueString);
		int numOfParas = 0;
//		if(!matcher.find()){
//			valueString.substring(valueString.indexOf("&enumerate(")+1, valueString.indexOf(""));
//		}
		int flag = 0;
		while(matcher.find()){
			parametersString = matcher.group(2);
			String[] parameters = parametersString.split(", ");
			numOfParas += parameters.length;
			flag++;
		}
		/*
		 * if flag == 0, it means there is no "&enumerate" funclet in quoted in valueString.
		 */
		if(flag == 0){
			return -1;
		}
		/*
		 * Empty parameter
		 */
		if(numOfParas == 0){
			return 1;
		}
		return numOfParas;
		
	}
	
	/**
	 * A collection for recording missed sections.
	 * @param ini
	 * @return
	 */
	public List<String> sectionMissingList(Ini ini){
		List<String> outputList = new ArrayList<String>();
		Map<String, Boolean> sectionCheckMap = ini.getSectionNumberCheckMap();
		for (String o : sectionCheckMap.keySet()) {
		      if (sectionCheckMap.get(o).equals(false)) {
		        outputList.add(o);
		      }
		}
		return outputList;
	}
	
	/**
	 * Print missed sections.
	 * @param ini
	 * @return
	 * @throws SemanticException 
	 */
	public boolean sectionMissingCheckAndPrint(Ini ini) throws SemanticException{
		List<String> outputList = this.sectionMissingList(ini);
		if(!outputList.isEmpty()){
			throw new SemanticException("Semantic Error 5: Defualt Section: "+outputList.toString()+ "Missing.", true);
		}else{
			System.out.println("No Missing Default Sections.");	
		}
		return true;
	}
	
	public static long parseInterval(final String s)
    {
        final Pattern p = Pattern.compile("^(\\d{1}):(\\d{2}):(\\d{2})$");
        final Matcher m = p.matcher(s);
        if (m.matches())
        {
            final long hr = Long.parseLong(m.group(1)) * TimeUnit.HOURS.toMillis(1);
            final long min = Long.parseLong(m.group(2)) * TimeUnit.MINUTES.toMillis(1);
            final long sec = Long.parseLong(m.group(3)) * TimeUnit.SECONDS.toMillis(1);
            return hr + min + sec;
        }
        else
        {
            throw new IllegalArgumentException(s + " is not a supported interval format!");
        }
    }

    public static String formatInterval(final long l)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        return String.format("%01d:%02d:%02d", hr, min, sec);
    }
	
	
	/**
	 * Display generic tree
	 * @param root
	 * @param level
	 */
	public void displayTree(ExecutionNode root, int level){
		String preString = "-";
		if(level == 0){
			System.out.println(preString+root.getReferencedObject().getFullName()
					+"  ( Total Runtime: "
					+formatInterval(root.getRuntime())+" )");
		}
		for(int i = 0; i<level; i++){
			preString += "--";
		}
		
		for(int i = 0; i<root.getChildren().size(); i ++){
			ExecutionNode eNode = root.getChildren().get(i);
			BasicSection eNodeReferencedBasicSection = eNode.getReferencedObject();
			if(eNodeReferencedBasicSection instanceof TestRunSection){
				System.out.println(preString+"-"+eNode.getReferencedObject().getFullName()
						+"  ( individual runtime: "+eNode.getIndividual_runtime()
						+" )  ( total runtime: "+formatInterval(eNode.getRuntime())+" )");
				int n = ((TestRunSection)(eNode.getReferencedObject())).getTestRuns().size();
				System.out.println(preString+"-"+"( "+n+" tests )" );
			}else if(eNodeReferencedBasicSection instanceof TestBuildSection){
				System.out.println(preString+"-"+eNode.getReferencedObject().getFullName()
						+"  ( individual runtime: "+eNode.getIndividual_runtime()
						+" )  ( total runtime: "+formatInterval(eNode.getRuntime())+" )");
				BasicSection mpi_getBasicSection = eNode.getParent().getParent().getParent().getReferencedObject();
				List<BasicSection> mpi_detailsBasicSections = mpi_getBasicSection.getRelatedSections();
				String output = "";
				for(BasicSection mpi_detailBasicSection: mpi_detailsBasicSections){
					Map<String, Integer> enumMap = mpi_detailBasicSection.getExecEnumerateMap();
					output += preString+"-" + mpi_detailBasicSection.getFullName()+ 
							" \n"+preString+"-" +" Number of versions of \"exec\": ";
					if(enumMap.isEmpty()){
						output += " ( x 1 ) ( no enumerate funclet in \"exec\")";
					}else{
						int num = 1;
						for(Map.Entry<String, Integer> entry:enumMap.entrySet()){
							output += " ( " + entry.getKey()+" x "+entry.getValue()+" ) ";
							num *= entry.getValue();
						}
						output += " :  x "+num+" in total";
					}
				}
				System.out.println(output);
			}
			
			else{
				System.out.println(preString+"-"+eNode.getReferencedObject().getFullName()
						+"  ( individual runtime: "+eNode.getIndividual_runtime()
						+" )  ( total runtime: "+formatInterval(eNode.getRuntime())+" )");
			}
			if(! eNode.getChildren().isEmpty()){
				displayTree(eNode, level+1);
			}
		}
	}
	
	/**
	 * Print execution tree.
	 * @param root
	 * @return
	 */
	public String printExecutionTree(ExecutionNode root){
		if(root.getChildren().isEmpty()){
			System.err.println("Execution tree is empty.");
			System.exit(0);
		}
		int indent = 0;
		StringBuilder sb = new StringBuilder();
		printExecutionTree(root, indent, sb);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param root
	 * @param indent
	 * @param sb
	 */
	private void printExecutionTree(ExecutionNode root, int indent, StringBuilder sb){
		if(root.getChildren().isEmpty()){
			System.err.println("Execution tree is empty.");
			System.exit(0);
		}
		sb.append(getIndentString(indent));
		if(root.getReferencedObject().getVariable() == ""){
			sb.append(root.getReferencedObject().getName());
		}
		else{
			sb.append(root.getReferencedObject().getName()+": "+root.getReferencedObject().getVariable());
		}
		sb.append(System.lineSeparator());
		for(ExecutionNode node: root.getChildren()){
			if(node.getChildren().isEmpty()){
				printNode(node,indent+1,sb);
			}
			else{
				printExecutionTree(node, indent+1, sb);
			}
		}
	}
	
	/**
	 * 
	 * @param indent
	 * @return
	 */
	private String getIndentString(int indent) { 
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < indent; i++) {
	    	sb.append("|  ");
	    }
	    return sb.toString();
	}
	
	/**
	 * 
	 * @param node
	 * @param indent
	 * @param sb
	 */
	private void printNode(ExecutionNode node, int indent, StringBuilder sb){
		sb.append(getIndentString(indent));
		sb.append("|-");
		if(node.getReferencedObject().getVariable().isEmpty()){
			sb.append(node.getReferencedObject().getName());
		}
		else{
			sb.append(node.getReferencedObject().getName()+": "+node.getReferencedObject().getVariable());
		}
		sb.append(System.lineSeparator());
		
	}
}
