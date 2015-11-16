package org.kutty.dbo;

import java.util.Arrays;

/** 
 * Defines a Generic Feature object which contains an array of 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 November,2015
 */
public class Feature {
	
	private int dimension;
	private Double [] featureVector;
	private int classLabel; 
	
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
		for (int i = 0; i < featureVector.length; i++) { 
			
			this.featureVector[i] = featureVector[i];
		}
	} 
	
	/**
	 * @return the classLabel
	 */
	public int getClassLabel() { 
		
		return classLabel;
	} 
	
	/**
	 * @param classLabel the classLabel to set
	 */
	public void setClassLabel(int classLabel) { 
		
		this.classLabel = classLabel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Feature [dimension=" + dimension + ", featureVector="
				+ Arrays.toString(featureVector) + ", classLabel=" + classLabel
				+ ", getDimension()=" + getDimension()
				+ ", getFeatureVector()=" + Arrays.toString(getFeatureVector())
				+ ", getClassLabel()=" + getClassLabel() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
