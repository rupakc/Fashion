package org.kutty.dbo;

import java.util.Map;

/** 
 * NFold Object to be utilized for NFold Cross Validation
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 5 November,2015
 */
public class NFold {
	
	public int splitNumber;
	public Map<String,Double> featureMap;
	public Double accuracy;
	
	/**
	 * @return the splitNumber
	 */
	public int getSplitNumber() { 
		
		return splitNumber;
	} 
	
	/**
	 * @param splitNumber the splitNumber to set
	 */
	public void setSplitNumber(int splitNumber) { 
		
		this.splitNumber = splitNumber;
	} 
	
	/**
	 * @return the featureMap
	 */
	public Map<String, Double> getFeatureMap() { 
		
		return featureMap;
	}
	
	/**
	 * @param featureMap the featureMap to set
	 */
	public void setFeatureMap(Map<String, Double> featureMap) { 
		
		this.featureMap = featureMap;
	}
	
	/**
	 * @return the accuracy
	 */
	public Double getAccuracy() { 
		
		return accuracy;
	}
	
	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(Double accuracy) {
		
		this.accuracy = accuracy;
	}
}
