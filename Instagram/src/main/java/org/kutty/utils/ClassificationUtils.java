package org.kutty.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/** 
 * Defines a set of utility functions to aid in the classification task
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 October, 2015
 */

public class ClassificationUtils {
	
	/** 
	 * Normalizes a probability array and converts it to a percentage
	 * @param probArray Double array containing the percentage probabilities
	 */
	public static void convertToPercentage(double probArray[]) { 

		double sum = 0.0;

		for (int i = 0; i < probArray.length; i++) { 
			sum = sum + probArray[i];
		}

		for (int i = 0; i < probArray.length; i++) { 
			probArray[i] = (probArray[i]/sum)*100;
		}
	}
	
	/** 
	 * Given the maximum entry returns the Ngram model number
	 * @param maxEntry Entry <String,Double> containing underscore separated values
	 * @return Integer corresponding to the Ngram model (in our case this values of either 1,2 or 3)
	 */
	public static int getNGramNumber(Entry<String,Double> maxEntry) { 
		
		String ngram = maxEntry.getKey();
		int ngramNum = 0;
		int indexStart = -1;
		int indexEnd = -1;
		
		indexStart = ngram.indexOf("_");
		
		if (indexStart != -1) { 
			indexEnd = ngram.indexOf("_",indexStart+1);
		}
		
		if (indexEnd != -1) { 
			ngramNum = Integer.valueOf(ngram.substring(indexStart+1, indexEnd).trim());
		}
		
		return ngramNum;
	}
	
	/** 
	 * Given the maximum entry returns the model number
	 * @param maxEntry Entry <String,Double> containing underscore separated values
	 * @return Integer corresponding to the model (in our case this values of either 1,2...5)
	 */
	public static int getModelNumber(Entry<String,Double> maxEntry) { 
		
		String model = maxEntry.getKey();
		int modelNum = 0;
		int indexStart = -1;
		
		indexStart = model.lastIndexOf("_");
		
		if (indexStart != -1) { 
			modelNum = Integer.valueOf(model.substring(indexStart+1).trim());
		}
		
		return modelNum;
	}
	
	/** 
	 * Multiplies the class label with the corresponding weight 
	 * @param label String containing the class label
	 * @param weight Integer containing the weight
	 * @return List<String> containing the repeated class labels
	 */
	public static List<String> getAugmentedLabelList(String label,int weight) { 
		
		List<String> labelList = new ArrayList<String>();
		
		for (int i = 1; i <= weight; i++) { 
			
			labelList.add(label);
		}
		
		return labelList;
	}
}
