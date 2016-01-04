package model;

import java.io.Serializable;

public class Install implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7490368714140497406L;
	private String mpi_name = "";
	private String mpi_version = "";
	private String avgRunTime = "";
	
	public Install(){}
	
	public Install(String mpi_name, String mpi_version, String avgRunTime){
		this.mpi_name = mpi_name;
		this.mpi_version = mpi_version;
		this.avgRunTime = avgRunTime;
	}
	/**
	 * @return the mpi_name
	 */
	public String getMpi_name() {
		return mpi_name;
	}
	/**
	 * @param mpi_name the mpi_name to set
	 */
	public void setMpi_name(String mpi_name) {
		this.mpi_name = mpi_name;
	}
	/**
	 * @return the mpi_version
	 */
	public String getMpi_version() {
		return mpi_version;
	}
	/**
	 * @param mpi_version the mpi_version to set
	 */
	public void setMpi_version(String mpi_version) {
		this.mpi_version = mpi_version;
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
