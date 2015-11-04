package org.kutty.dbo;

import java.util.List;
import java.util.Map;

/** 
 * Defines the confusion matrix which is to be used for benchmarking and other tests
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 3 November,2015
 */
public class ConfusionMatrix {
	
	private List<String> classLabelSet;
	private double matrix[][];
	private Map<String,Integer> classMapping;
	
	/**
	 * @return the classLabelSet
	 */
	public List<String> getClassLabelSet() {
		return classLabelSet;
	}
	
	/**
	 * @param classLabelSet the classLabelSet to set
	 */
	public void setClassLabelSet(List<String> classLabelSet) {
		this.classLabelSet = classLabelSet;
	}
	
	/**
	 * @return the matrix
	 */
	public double[][] getMatrix() {
		return matrix;
	}
	
	/**
	 * @param matrix the matrix to set
	 */
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * @return the classMapping
	 */
	public Map<String, Integer> getClassMapping() {
		return classMapping;
	}

	/**
	 * @param classMapping the classMapping to set
	 */
	public void setClassMapping(Map<String, Integer> classMapping) {
		this.classMapping = classMapping;
	}
}
