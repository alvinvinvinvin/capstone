/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import generatedCode.INIGrammar_1Parser;


/**
 * @author alvin
 *
 */
public class Convertor {
	public Convertor(){}
	/**
	 * Convert different section syntax tree node to objects based on their
	 * section names.
	 * @param ini
	 * @param ctx
	 * @return BasicSection
	 * @throws SemanticException 
	 */
	 public BasicSection convertSyntaxTreeToObjects
	 (Ini ini, INIGrammar_1Parser.SectionContext ctx) throws SemanticException{
		 String sectionNameString = ctx.section_name().section_name_head().getText().trim();
		 //If there were exact three nodes under section_name node, it means the structure of section_name node is
		 // "[" + "section_head" + "]"
			if(ctx.section_name().getChildCount()==3){
				switch (sectionNameString) {
				case "MTT":
					MTTSection newMttSection = new MTTSection();
					newMttSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionNameString));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString, newMttSection);
					ini.recordSectionName(sectionNameString);
					return(newMttSection);			
				default:
					CustomizedSection newCustomizedSection = new CustomizedSection(sectionNameString);
					newCustomizedSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionNameString));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString, newCustomizedSection);
					
					return(newCustomizedSection);
				}
			}else{
				String sectionVariableString = ctx.section_name().section_name_variable().getText();
				String trimedVariable = sectionVariableString.trim();
				String sectionFullName = sectionNameString+trimedVariable;
				switch(sectionNameString){
				case "MPI get":
					MPIGetSection newMpiGetSection =  new MPIGetSection(trimedVariable);
					newMpiGetSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newMpiGetSection);
					ini.recordSectionName(sectionNameString);
					
					return(newMpiGetSection);
				case "MPI install":
					MPIInstallSection newInstallSection = new MPIInstallSection(trimedVariable);
					newInstallSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newInstallSection);
					ini.recordSectionName(sectionNameString);
					
					return(newInstallSection);
				case "MPI Details":
					MPIDetailsSection newDetailsSection = new MPIDetailsSection(trimedVariable);
					newDetailsSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newDetailsSection);
					ini.recordSectionName(sectionNameString);
					
					return(newDetailsSection);
				case "Test get":
					TestGetSection newGetSection = new TestGetSection(trimedVariable);
					newGetSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newGetSection);
					ini.recordSectionName(sectionNameString);
					
					return(newGetSection);
				case "Test build":
					TestBuildSection newBuildSection = new TestBuildSection(trimedVariable);
					newBuildSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newBuildSection);
					ini.recordSectionName(sectionNameString);
					
					return(newBuildSection);
				case "Test run":
					TestRunSection newTestRunSection = new TestRunSection(trimedVariable);
					newTestRunSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newTestRunSection);
					ini.recordSectionName(sectionNameString);
					
					return(newTestRunSection);
				case "Reporter":
					ReporterSection newReporterSection = new ReporterSection(trimedVariable);
					newReporterSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newReporterSection);
					ini.recordSectionName(sectionNameString);
					
					return(newReporterSection);
				default:
					CustomizedSection newCustomizedSection = new CustomizedSection(sectionNameString, trimedVariable);
					newCustomizedSection.setKeyValuePairs(convertKVpairs(ctx.section_body(), sectionFullName));
					ini.addNameObjectPairsToSectionNameMap(sectionNameString+":"+trimedVariable, newCustomizedSection);
					
					return(newCustomizedSection);
				}
			}
	 }
	 
		/**
		* This function is going to convert a syntax tree to
		* an key-value string map.
		* @param thisBS
		* @param ctxList
		* @return
		 * @throws SemanticException 
		*/
		public KeyValuePair<String, String> convertKVpairs
		(INIGrammar_1Parser.Section_bodyContext section_bodyContext, String sectionName) throws SemanticException{
			KeyValuePair<String, String> kvmap = new BaseKeyValuePair<String, String>();
			String key = "";
			String value = "";
			/*
			 * Since there is only one context rule in "section_body" called "kv_pair",
			 * we can just simply get kv_pairs by using "section_bodyContext.kv_pair()".
			 * There will be nothing missed. Unless we changed the rule in g4 file.
			 */
			List<INIGrammar_1Parser.Kv_pairContext> kv_pairs =  section_bodyContext.kv_pair();
			for(INIGrammar_1Parser.Kv_pairContext kv_pair: kv_pairs){
				key = kv_pair.key().getText().trim();
				
				if(kv_pair.getChildCount() <3){
					kvmap.put(key, value);
				}else if (kv_pair.getChildCount() == 3){
					value = kv_pair.vrhs().getText().trim();
					kvmap.put(key, value);
				}else{
					throw new SemanticException(
							"Semantic Error: " +"\n"+"Invalid KVP convertion at Section:" 
					+sectionName+ ", key: "+key, false);
				}
			}
			return kvmap;
		}
		
		/**
		 * 	Trying to build an execution tree based on section map. Those nodes are relatively
		 * up to those strings' relation in the beginning of function. More details please check
		 * the design diagram in notebook.
		 * The execution tree here basically separated to two trees. First one is MPI Install tree
		 * Second one is Test Run tree. For each leaf of MPI install tree, it connects to a
		 * whole test run tree copy.
		 * MPI Install Tree contains two layers. The first layer is MPI Get layer. Second layer
		 * is MPI Install layer. Their relations are based on the referenced key-value pairs
		 * with fixed key words.
		 * Similarly, Test Run tree has three layers: Test get, Test build, and Test run.
		 * @param inputMap
		 * @return
		 * @throws Throwable 
		 */
		public ExecutionNode buildExecutionTree(Map<String, BasicSection> inputMap) throws Throwable{
			/*
			 * Set root for MPI installation phases, of course the root is "MTT"
			 * Since BasicSection is executionNode already, so we don't have to do particular
			 * things here.
			 */
			DatabaseConnector databaseConnector = new DatabaseConnector();
			
			Map<String, ExecutionNode> mpi_getSections = new HashMap<String, ExecutionNode>();
			Map<String, ExecutionNode> mpi_installSections = new HashMap<String, ExecutionNode>();
			Map<String, ExecutionNode> test_getSections = new HashMap<String, ExecutionNode>();
			Map<String, ExecutionNode> test_buildSections = new HashMap<String, ExecutionNode>();
			Map<String, ExecutionNode> test_runSections = new HashMap<String, ExecutionNode>();
			
			String mtt_PhaseNameString = KeyWords.MTT_PHASENAME.toString();
			String mpi_getPhaseNameString = KeyWords.MPI_GET_PHASENAME.toString();
			String mpi_installPhaseNameString = KeyWords.MPI_INSTALL_PHASENAME.toString();
			String test_getPhaseNameString = KeyWords.TEST_GET_PHASENAME.toString();
			String test_buildPhaseNameString = KeyWords.TEST_BUILD_PHASENAME.toString();
			String test_runPhaseNameString = KeyWords.TEST_RUN_PHASENAME.toString();
			
			ExecutionNode mttSectionNode = new ExecutionNode(inputMap.get(mtt_PhaseNameString));
			
			
			for(BasicSection bs: inputMap.values()){
				if(bs.getName().equals(mpi_getPhaseNameString)){
					List<BasicSection> mpi_detailSections = bs.getRelatedSections();
					for(BasicSection mpi_detailSection: mpi_detailSections){
						((MPIGetSection)bs).getAllMpiDetailsExecEnumerateNameAndNum().add
						(mpi_detailSection.getExecEnumerateMap());
					}
					mpi_getSections.put(bs.getFullName(), new ExecutionNode(bs));
				}else if(bs.getName().equals(mpi_installPhaseNameString)){
					mpi_installSections.put(bs.getFullName(), new ExecutionNode(bs));
				}else if(bs.getName().equals(test_getPhaseNameString)){
					test_getSections.put(bs.getFullName(), new ExecutionNode(bs));
				}else if(bs.getName().equals(test_buildPhaseNameString)){
					String avg_runTimeString = databaseConnector.test_buildRuntimeRequest(bs.getVariable());
					if(avg_runTimeString.equals("null") || avg_runTimeString.equals("")){
						avg_runTimeString = KeyWords.RUNTIME_ONE_MIN.toString();
						((TestBuildSection)bs).setTestBuild(new TestBuild(bs.getVariable(), avg_runTimeString));
					}else{
						
						((TestBuildSection)bs).setTestBuild(new TestBuild(bs.getVariable(), avg_runTimeString));
					}
					ExecutionNode newNode = new ExecutionNode(bs);
					newNode.setIndividual_runtime(avg_runTimeString);
					test_buildSections.put(bs.getFullName(), newNode);
				}else if(bs.getName().equals(test_runPhaseNameString)){
					List<String> test_nameList = databaseConnector.testSuiteRequest(bs.getVariable());
					long totalRuntime = 0;
					for(String t: test_nameList){
						String avg_runtimeString = databaseConnector.test_runRuntimeRequest(t);
						
						if(avg_runtimeString.equals("null") || avg_runtimeString.equals("")){
							avg_runtimeString = KeyWords.RUNTIME_ZERO.toString();
							((TestRunSection)bs).getTestRuns().add(new TestRun(t, avg_runtimeString));
						}else{
							((TestRunSection)bs).getTestRuns().add(new TestRun(t, avg_runtimeString));
							totalRuntime += CommonHelper.parseInterval(avg_runtimeString);
						}
					}
					ExecutionNode newNode = new ExecutionNode(bs);
					newNode.setRuntime(totalRuntime);
					test_runSections.put(bs.getFullName(), newNode);
				}else{
					
				}
			}
			
			/*
			 * Set each MPI get section as children to root node.
			 */

			mttSectionNode.setChildren(new ArrayList<ExecutionNode>(mpi_getSections.values()));
			
			/*
			 * Check MPI install section related MPI get sections, set this MPI install section as child
			 * to that MPI get section.
			 */
			List<ExecutionNode> mpi_installs_ExecutionTreeNodes = new ArrayList<ExecutionNode>();
			List<ExecutionNode> test_get_ExecutionTreeNodes = new ArrayList<ExecutionNode>();
			List<ExecutionNode> test_build_ExecutionTreeNodes = new ArrayList<ExecutionNode>();
			List<ExecutionNode> test_run_ExecutionTreeNodes = new ArrayList<ExecutionNode>();
			
			for(ExecutionNode en: mpi_installSections.values()){
				List<BasicSection> mpiGetSections = en.getReferencedObject().getRelatedSections();
				for(BasicSection mpi_getBasicSection: mpiGetSections){
					/*
					 * Deep copy the "en" and set copied one's parent as current mpi_get section object.
					 * But before that, use "findChild" to locate where that mpi_get section is.
					 */
					String mpi_nameString = mpi_getBasicSection.getVariable();
					String mpi_versionString = "dev-641-gc857cc9";
					String avg_runtimeString = databaseConnector.installRuntimeRequest(mpi_nameString, mpi_versionString);
					if(avg_runtimeString.equals("null") || avg_runtimeString.equals("")){
						avg_runtimeString = KeyWords.RUNTIME_ONE_MIN.toString();
					}
					ExecutionNode newExecutionNode = en.deepCopy(en);
					newExecutionNode.setParent(mttSectionNode.findChild(mpi_getBasicSection, mttSectionNode));
					((MPIInstallSection)newExecutionNode.getReferencedObject()).setInstall(new Install(mpi_nameString, mpi_versionString, avg_runtimeString));
					newExecutionNode.setIndividual_runtime(avg_runtimeString);
					mpi_installs_ExecutionTreeNodes.add(newExecutionNode);
				}
			}
			/*
			 * MPI installation phases done
			 */
			
			/*
			 * Build test running phases. test get is the root.
			 * Before that, set all test get node as children to each of mpi install sections.
			 */
			/*
			 * Set every test get root as child to MPI install section.
			 */
			for(ExecutionNode test_getExecutionNode: test_getSections.values()){
				for(ExecutionNode mpi_installs_node: mpi_installs_ExecutionTreeNodes){
					ExecutionNode newExecutionNode = test_getExecutionNode.deepCopy(test_getExecutionNode);
					newExecutionNode.setParent(mpi_installs_node);
					test_get_ExecutionTreeNodes.add
					(newExecutionNode);
				}
			}
			/*
			 * Check each test build, get related test get sections, add this test build section as child
			 * to that test get section.
			 */
			for(ExecutionNode test_buildExecutionNode: test_buildSections.values()){
				List<BasicSection> test_getBasicSections = 
						test_buildExecutionNode.getReferencedObject().getRelatedSections();
				
				for(BasicSection test_getSection: test_getBasicSections){
					for(ExecutionNode test_getExecutionNode: test_get_ExecutionTreeNodes){
						String nameOfTestGetExecutionNode = test_getExecutionNode.getReferencedObject().getFullName();
						String nameOfTestGetSectionObject = test_getSection.getFullName();
						if(nameOfTestGetExecutionNode.equals(nameOfTestGetSectionObject)){
							ExecutionNode newExecutionNode = test_buildExecutionNode.deepCopy(test_buildExecutionNode);
							newExecutionNode.setParent(test_getExecutionNode);
							test_build_ExecutionTreeNodes.add
							(newExecutionNode);
						}
					}
				}
			}
			/*
			 * Check each test run, get related test build sections, add this test run section as child
			 * to that test build section.
			 */
			for(ExecutionNode test_runExecutionNode : test_runSections.values()){
				List<BasicSection> test_buildBasicSections = 
						test_runExecutionNode.getReferencedObject().getRelatedSections();
				
				for(BasicSection test_buildBasicSection: test_buildBasicSections){
					for(ExecutionNode test_buildExecutionNode: test_build_ExecutionTreeNodes){
						if(test_buildExecutionNode.getReferencedObject().getFullName().equals
								(test_buildBasicSection.getFullName())){
							ExecutionNode newExecutionNode = test_runExecutionNode.deepCopy(test_runExecutionNode);
							newExecutionNode.setParent(test_buildExecutionNode);
							test_run_ExecutionTreeNodes.add
							(newExecutionNode);
						}
					}
				}
			}
			
			/*
			 * Calculate runtime and assign it to corresponding execution node.
			 */
			int enumerateTimes = 1;
			
			for(ExecutionNode mpi_getExecutionNode: mttSectionNode.getChildren()){
				List<Map<String, Integer>> enumerateRuntimeMaps = 
						((MPIGetSection)(mpi_getExecutionNode.getReferencedObject())).getAllMpiDetailsExecEnumerateNameAndNum();
				int time = 1;
				for(Map<String, Integer> map : enumerateRuntimeMaps){
					for(Map.Entry<String, Integer> entry:map.entrySet()){
						time *= entry.getValue();
					}
				}
				enumerateTimes = time;
			}
			
			for(ExecutionNode test_runExecutionNode: test_run_ExecutionTreeNodes){
				ExecutionNode test_buildExecutionNode = test_runExecutionNode.getParent();
				long test_buildExecutionNode_runtime = test_buildExecutionNode.getRuntime();
				test_buildExecutionNode_runtime += test_runExecutionNode.getRuntime();
				test_buildExecutionNode.setRuntime(test_buildExecutionNode_runtime*enumerateTimes);
			}
			
			for(ExecutionNode test_build_ExecutionNode: test_build_ExecutionTreeNodes){
				long test_buildExecutionNode_runtime = test_build_ExecutionNode.getRuntime();
				String test_build_buildRuntimeString= 
						((TestBuildSection)test_build_ExecutionNode.getReferencedObject()).getTestBuild().getAvgRunTime();
				test_buildExecutionNode_runtime += CommonHelper.parseInterval(test_build_buildRuntimeString);
				test_build_ExecutionNode.setRuntime(test_buildExecutionNode_runtime);
			}
			
			for(ExecutionNode test_getExecutionNode: test_get_ExecutionTreeNodes){
				long test_getRuntime = test_getExecutionNode.getRuntime();
				for(ExecutionNode test_build_ExecutionNode: test_getExecutionNode.getChildren()){
					test_getRuntime += test_build_ExecutionNode.getRuntime();
				}
				test_getExecutionNode.setRuntime(test_getRuntime);
			}
			
			for(ExecutionNode mpi_installExecutionNode: mpi_installs_ExecutionTreeNodes){
				String mpi_install_installRuntimeString = 
						((MPIInstallSection)mpi_installExecutionNode.getReferencedObject()).getInstall().getAvgRunTime();
				long mpi_install_installRuntimeLong = CommonHelper.parseInterval(mpi_install_installRuntimeString);
				for(ExecutionNode test_getExecutionNode: mpi_installExecutionNode.getChildren()){
					mpi_install_installRuntimeLong += test_getExecutionNode.getRuntime();
				}
				mpi_installExecutionNode.setRuntime(mpi_install_installRuntimeLong);
			}
			
			for(ExecutionNode mpi_getExecutionNode: mttSectionNode.getChildren()){
				long mpi_getRuntimeString = mpi_getExecutionNode.getRuntime();
				for(ExecutionNode mpi_installExecutionNode : mpi_getExecutionNode.getChildren()){
					mpi_getRuntimeString += mpi_installExecutionNode.getRuntime();
				}
				mpi_getExecutionNode.setRuntime(mpi_getRuntimeString);
			}
			
			long total_runtimeLong = mttSectionNode.getRuntime();;
			for(ExecutionNode mpi_getExecutionNode: mttSectionNode.getChildren()){
				total_runtimeLong += mpi_getExecutionNode.getRuntime();
			}
			mttSectionNode.setRuntime(total_runtimeLong);

			return mttSectionNode;
		}

}
