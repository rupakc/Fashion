package org.kutty.dbo;

/** 
 * Defines an update Object for off-line update of the trained models
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 October, 2015
 */

public class Update {
	
	private int ngramNum;
	private int modelNum;
	private double probPercent;
	private String classLabel; 
	
	/**
	 * @return the ngramNum
	 */
	public int getNgramNum() {
		
		return ngramNum;
	} 
	
	/**
	 * @param ngramNum the ngramNum to set
	 */
	public void setNgramNum(int ngramNum) { 
		
		this.ngramNum = ngramNum;
	} 
	
	/**
	 * @return the modelNum
	 */
	public int getModelNum() { 
		
		return modelNum;
	} 
	
	/**
	 * @param modelNum the modelNum to set
	 */
	public void setModelNum(int modelNum) { 
		
		this.modelNum = modelNum;
	}
	
	/**
	 * @return the probPercent
	 */
	public double getProbPercent() { 
		
		return probPercent;
	} 
	
	/**
	 * @param probPercent the probPercent to set
	 */
	public void setProbPercent(double probPercent) { 
		
		this.probPercent = probPercent;
	} 
	
	/**
	 * @return the classLabel
	 */
	public String getClassLabel() { 
		
		return classLabel;
	} 
	
	/**
	 * @param classLabel the classLabel to set
	 */
	public void setClassLabel(String classLabel) { 
		
		this.classLabel = classLabel;
	}
}
