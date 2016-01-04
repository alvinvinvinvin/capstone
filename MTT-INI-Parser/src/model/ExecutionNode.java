package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang3.SerializationUtils;

public class ExecutionNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7732637177440906489L;
	/**
	 * 
	 */
	private long runtime = 0;
	private String individual_runtime = KeyWords.RUNTIME_ZERO.toString();
	private BasicSection referencedObject = null;
	private ExecutionNode parent = null;
	private List<ExecutionNode> children = new ArrayList<ExecutionNode>();
	
	public ExecutionNode(){
		super();
	}
	
	public ExecutionNode(BasicSection section){
		super();
		this.setReferencedObject(section);
	}
	
	public ExecutionNode getChild(int i){
		return this.getChildren().get(i);
	}
	
	public void addChild(ExecutionNode child){
		child.parent = this;
		this.children.add(child);
	}
	
	
	/**
	 * @return the indiivdual_runtime
	 */
	public String getIndividual_runtime() {
		return individual_runtime;
	}

	/**
	 * @param indiivdual_runtime the indiivdual_runtime to set
	 */
	public void setIndividual_runtime(String individual_runtime) {
		this.individual_runtime = individual_runtime;
	}

	public long getRuntime() {
		return runtime;
	}
	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}
	public BasicSection getReferencedObject() {
		return referencedObject;
	}
	public void setReferencedObject(BasicSection referencedObject) {
		this.referencedObject = referencedObject;
	}
	public ExecutionNode getParent() {
		return parent;
	}
	public void setParent(ExecutionNode parent) {
		parent.children.add(this);
		this.parent = parent;	
	}
	public List<ExecutionNode> getChildren() {
		return children;
	}
	public void setChildren(List<ExecutionNode> children) {
//		Iterator<ExecutionNode> childIterator = children.iterator();
//		while(childIterator.hasNext()){
//			ExecutionNode child = childIterator.next();
//			child.setParent(this);
//			childIterator.remove();
//		}
		for(ExecutionNode en: children){
			en.setParent(this);
		}
		this.children = children;
	}
	
	
	/**
	 * Recursively find child node based on input referenced object.
	 * @param referencedObject
	 * @param exeNode
	 * @return
	 */
	public ExecutionNode findChild(BasicSection referencedObject, ExecutionNode exeNode){
		if(exeNode.getReferencedObject() == null){
			return null;
		}
		if(exeNode.getReferencedObject().equals(referencedObject)){
			return exeNode;
		}
		if(exeNode.getChildren().isEmpty()){
			return null;
		}
		else{
			int size = exeNode.getChildren().size();
			for(int i = 0; i<size; i++){
				if(exeNode.getChildren().get(i).getReferencedObject().getFullName().equals(referencedObject.getFullName())){
					return exeNode.getChildren().get(i);
				}
				else{
					if(exeNode.findChild(referencedObject, exeNode.getChildren().get(i)) == null){
						continue;
					}
					else{
						return exeNode.findChild(referencedObject, exeNode.getChildren().get(i));
					}
				}
			}
			return null;
		}
	}
	
	/**
	 * 
	 * @param another
	 * @param parentNode
	 * @return
	 */
	public ExecutionNode deepCopy(ExecutionNode another) throws CloneNotSupportedException{
		ExecutionNode newExecutionNode = null;
		newExecutionNode = SerializationUtils.clone(another);
		
		return newExecutionNode;
	}
	
}
