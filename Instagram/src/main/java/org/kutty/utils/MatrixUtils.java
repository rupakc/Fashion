package org.kutty.utils;

/** 
 * Defines a set of utility functions used for matrix operations
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 4 November,2015
 */
public class MatrixUtils {
	
	/** 
	 * Calculates the sum of the principal diagonal of a square matrix
	 * @param matrix Matrix whose diagonal sum is to be calculated
	 * @return Double containing the sum of the diagonal elements
	 */
	public static <T> double getDiagonalSum(T [][] matrix) { 
		
		double sum = 0.0;
		
		for (int i = 0; i < matrix.length; i++) {  
			
			sum = sum + (Double) matrix[i][i];
		}
		
		return sum;
	}
	
	/** 
	 * Returns the sum of a given row of the matrix (the numbering starts from 1)
	 * @param matrix Matrix whose row sum is to be calculated
	 * @param rowNum Integer containing the row number (starting with 1)
	 * @return Double containing the sum of the row
	 */
	public static <T> double getRowSum(T [][] matrix,int rowNum) { 
		
		double rowSum = 0.0;
		rowNum = rowNum-1;
		
		for (int i = 0; i < matrix[rowNum].length; i++) { 
			
			rowSum = rowSum + (Double) matrix[rowNum][i];
		}
		
		return rowSum;
	}
	
	/** 
	 * Returns the sum of a given column of the matrix (the numbering starts from 1)
	 * @param matrix Matrix whose column sum is to be calculated
	 * @param colNum Integer containing the column number (starting with 1)
	 * @return Double containing the sum of the column
	 */
	public static <T> double getColumnSum(T [][] matrix, int colNum) { 
		
		double colSum = 0.0;
		colNum = colNum-1;
		
		for (int i = 0; i < matrix.length; i++) {  
			
			colSum = colSum + (Double) matrix[i][colNum];
		}
		
		return colSum;
	}
	
	/** 
	 * Calculates the sum of all the elements in the matrix
	 * @param matrix Matrix whose sum is to be calculated
	 * @return Double containing the sum of the given matrix
	 */
	public static <T> double getMatrixSum(T [][] matrix) { 
		
		double sum = 0.0;
		
		for (int i = 0; i < matrix.length; i++) { 
			
			for (int j = 0; j < matrix[i].length; j++) { 
				
				sum = sum + (Double)matrix[i][j];
			}
		}
		
		return sum;
	}
	
	/** 
	 * Adds two given matrices (i.e. the matrices must be of the same order and compatible)
	 * @param first First matrix to add
	 * @param second Second matrix to be added
	 * @return A matrix containing the sum of the two
	 */
	public static <T> Double [][] addMatrix(T[][] first,T[][] second) { 
		
		Double [][] sum = new Double[first.length][second.length];
		
		for (int i = 0; i < first.length; i++) { 
			
			for (int j = 0; j < first[i].length; j++) { 
				
				sum[i][j] = (Double)first[i][j] + (Double)second[i][j];
			}
		}
		
		return sum;
	}
	
	/** 
	 * Given 2 vectors returns the hadamard product of them
	 * @param first First vector
	 * @param second Second vector
	 * @return Double containing the hadamard product
	 */
	public static <T> Double getHadamardProduct(T[] first, T[] second) { 
		
		double hadamard = 0.0; 
		
		for (int i = 0; i < first.length; i++) { 
			
			hadamard = hadamard + ((Double)first[i]*(Double)second[i]);
		}
		
		return hadamard;
	}
}
