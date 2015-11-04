package org.kutty.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kutty.dbo.Benchmark;
import org.kutty.dbo.ConfusionMatrix;

/** 
 * Defines a set of utility functions for testing model performance
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 11 October, 2015
 * TODO - Add precision, recall and N-fold Cross Validation, ROC (Area)
 */

public class PerformanceUtil {
	
	/** 
	 * Calculates the F1 score given the precision and recall
	 * @param precision Double containing the precision
	 * @param recall Double containing the recall
	 * @return Double containing the F1-score which is the harmonic mean of the two
	 */
	public static double f1Score(double precision,double recall) { 
		
		if (precision == 0 && recall == 0) { 
			
			return 0;
		}
		
		return (2*precision*recall)/(precision+recall);
	}
	
	/** 
	 * 
	 * @param testSet
	 * @return
	 */
	public static double getAccuracy(List<Benchmark> testSet) { 
		
		double accuracy = 0.0;
		int total = testSet.size() + 1;
		
		for (Benchmark benchmark : testSet) { 
			
			if (benchmark.getActualLabel().equalsIgnoreCase(benchmark.getPredictedLabel())) { 
				accuracy = accuracy + 1;
			}
		}
		
		return (accuracy/total);
	}
	
	/** 
	 * 
	 * @param result
	 * @return
	 */
	public static ConfusionMatrix getConfusionMatrix(List<Benchmark> result) { 
		
		ConfusionMatrix confusionMatrix = getClassMappingUtil(result);
		Map<String,Integer> classMap = confusionMatrix.getClassMapping();
		double matrix[][] = confusionMatrix.getMatrix();
		String predicted;
		String actual;
		
		for (Benchmark benchmark : result) {  
			
			predicted = benchmark.getPredictedLabel();
			actual = benchmark.getActualLabel();
			matrix[classMap.get(actual)][classMap.get(predicted)] += 1.0;
		}
		
		confusionMatrix.setMatrix(matrix);
		
		return confusionMatrix;
	}
	
	/** 
	 * 
	 * @param resultList
	 * @return
	 */
	public static ConfusionMatrix getClassMappingUtil(List<Benchmark> resultList) { 
		
		List<String> classLabel = new ArrayList<String>();
		Map<String,Integer> classMapping = new HashMap<String,Integer>();
		double [][] matrix;
		ConfusionMatrix confusionMatrix = new ConfusionMatrix();
		
		for (Benchmark benchmark : resultList) { 
			
			if (!classLabel.contains(benchmark.getActualLabel())) {
				
				classLabel.add(benchmark.getActualLabel());
			}
		}
		
		for (int i = 0; i < classLabel.size(); i++) {  
			
			classMapping.put(classLabel.get(i), i);
		}
		
		matrix = new double[classLabel.size()][classLabel.size()];
		
		confusionMatrix.setClassLabelSet(classLabel);
		confusionMatrix.setClassMapping(classMapping);
		confusionMatrix.setMatrix(matrix);
		
		return confusionMatrix;
	}
	
	public static double getMicroPrecision(List<Benchmark> resultList) { 
		
		double microPrecision = 0.0;
		double truePositives = 0.0;
		double predictedPositives = 1.0;
		
		ConfusionMatrix confusionMatrix = getConfusionMatrix(resultList);
		double [][] matrix = confusionMatrix.getMatrix();
		
		for (int i = 0; i < matrix.length; i++) { 
			
			for (int j = 0;j < matrix[i].length; j++) { 
				
				if (i == j) { 
					
					truePositives += matrix[i][j]; 
				} 
				
				predictedPositives += matrix[j][i];
			}
		}
		
		microPrecision = truePositives/predictedPositives;
		
		return microPrecision;
	}
	
	public static double getMacroPrecision(List<Benchmark> resultList)  { 
		
		double macroPrecision = 0.0;
		Map<String,Double> precisionPerClass = getPrecisionPerClass(resultList);
		for (String key : precisionPerClass.keySet()) { 
			macroPrecision = macroPrecision + precisionPerClass.get(key);
		}
		
		return (macroPrecision/precisionPerClass.size());
	}
		
	public static Map<String,Double> getPrecisionPerClass(List<Benchmark> resultList) { 
		
		ConfusionMatrix confusionMatrix = getConfusionMatrix(resultList);
		Map<String,Double> precisionMap = new HashMap<String,Double>();
		List<String> classLabels = confusionMatrix.getClassLabelSet();
		double [][] matrix = confusionMatrix.getMatrix();
		double [] precisionPerClass = new double[classLabels.size()];
		double predictedPositive = 1.0; 
		
		for (int i = 0; i < matrix.length; i++) {  
			
			predictedPositive = 1.0;
			
			for (int j = 0; j < matrix[i].length; j++) { 
				
				if (i == j) { 
					precisionPerClass[i] = matrix[i][j];
				}
				
				predictedPositive = predictedPositive + matrix[j][i];
			}
			
			precisionPerClass[i] = precisionPerClass[i]/predictedPositive;
			precisionMap.put(classLabels.get(i), precisionPerClass[i]);
		}
		
		return precisionMap;
	}
	
	public static Map<String,Double> getRecallPerClass(List<Benchmark> resultList) { 
		
		ConfusionMatrix confusionMatrix = getConfusionMatrix(resultList);
		Map<String,Double> recallMap = new HashMap<String,Double>();
		List<String> classLabels = confusionMatrix.getClassLabelSet();
		double [][] matrix = confusionMatrix.getMatrix();
		double [] recallPerClass = new double[classLabels.size()];
		double actualPositive = 1.0; 
		
		for (int i = 0; i < matrix.length; i++) {  
			
			actualPositive = 1.0;
			
			for (int j = 0; j < matrix[i].length; j++) { 
				
				if (i == j) { 
					
					recallPerClass[i] = matrix[i][j];
				}
				
				actualPositive = actualPositive + matrix[i][j];
			}
			
			recallPerClass[i] = recallPerClass[i]/actualPositive;
			recallMap.put(classLabels.get(i), recallPerClass[i]);
		}
		
		return recallMap;
	}
	
	public static double getMacroRecall(List<Benchmark> resultList) {  
		
		double macroRecall = 0.0;
		Map<String,Double> recallMap = getRecallPerClass(resultList); 
		
		for (String key : recallMap.keySet()) {  
			
			macroRecall = macroRecall + recallMap.get(key);
		}
		
		return (macroRecall/recallMap.size());
	}
	
	public static double getMicroRecall(List<Benchmark> resultList) { 
		
		double microRecall = 0.0;
		double truePositives = 0.0;
		double predictedPositives = 1.0;
		
		ConfusionMatrix confusionMatrix = getConfusionMatrix(resultList);
		double [][] matrix = confusionMatrix.getMatrix();
		
		for (int i = 0; i < matrix.length; i++) { 
			
			for (int j = 0;j < matrix[i].length; j++) { 
				
				if (i == j) { 
					
					truePositives += matrix[i][j]; 
				} 
				
				predictedPositives += matrix[i][j];
			}
		}
		
		microRecall = truePositives/predictedPositives;
		
		return microRecall;
	}
}
