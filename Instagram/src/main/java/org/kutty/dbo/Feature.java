package org.kutty.dbo;

/** 
 * Defines a Generic Feature object which contains an array of 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 November,2015
 */
public class Feature {
	
	private int dimension;
	private Double [] featureVector;
	private String classLabel; 
	
	/**
	 * @return the dimension
	 */
	public int getDimension() { 
		
		return dimension;
	}
	
	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(int dimension) { 
		
		this.dimension = dimension;
	} 
	
	/**
	 * @return the featureVector
	 */
	public Double[] getFeatureVector() { 
		
		return featureVector;
	} 
	
	/**
	 * @param featureVector the featureVector to set
	 */
	public void setFeatureVector(Double[] featureVector) { 
		
		this.featureVector = featureVector;
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
