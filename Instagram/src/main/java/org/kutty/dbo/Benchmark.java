package org.kutty.dbo;

/** 
 * Defines a generic benchmark object used for benchmarking purposes
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 3 October,2015
 */

public class Benchmark {
	
	private String actualLabel;
	private String predictedLabel;
	private String content;
	private String type;
	
	/**
	 * @return the actualLabel
	 */
	public String getActualLabel() { 
		
		return actualLabel;
	} 
	
	/**
	 * @param actualLabel the actualLabel to set
	 */
	public void setActualLabel(String actualLabel) { 
		
		this.actualLabel = actualLabel;
	} 
	
	/**
	 * @return the predictedLabel
	 */
	public String getPredictedLabel() {
		return predictedLabel;
	} 
	
	/**
	 * @param predictedLabel the predictedLabel to set
	 */
	public void setPredictedLabel(String predictedLabel) { 
		
		this.predictedLabel = predictedLabel;
	} 
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	} 
	
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	} 
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	} 
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
