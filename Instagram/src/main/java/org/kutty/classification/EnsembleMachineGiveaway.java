package org.kutty.classification;

import java.util.ArrayList;
import java.util.List;

/** 
 * Carries out Ensemble Learning for a given classifier 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 25 August, 2015
 * 
 */

public class EnsembleMachineGiveaway {
	
	/** 
	 * Aggregates different models to get a ensemble average of the result
	 * @param captionText String containing the captionText
	 * @param tagSet String containing the Tagset
	 * @param n Integer containing the number of models to be loaded (max 5 now)
	 * @return String containing the class label (i.e. real or fake)
	 */ 
	
	public String organizeAndActEnsemble(String captionText, String tagSet,int n) { 
		
		NaiveBayesGiveaway ensemble[] = new NaiveBayesGiveaway[n];
		List<String> result_list = new ArrayList<String>(); 
		String result = ""; 
		
		for (int i = 0; i < n; i++) { 
			
			ensemble[i] = new NaiveBayesGiveaway(i+1);
			result = ensemble[i].classifyGiveaway(captionText, tagSet);
			result = sanitizeString(result);
			result_list.add(result);
		}
		
		result = getMaxLabel(result_list);
		
		return result;
	} 
	
	/** 
	 * Given the output of different classifiers finds the most frequently occuring class
	 * @param result_list List containing the results
	 * @return Class label of the most frequently occuring class
	 */ 
	
	public static <T> T getMaxLabel(List<T> result_list) { 
		
		int count = 0;
		int max_count = Integer.MIN_VALUE; 
		T temp; 
		T max_label = null; 
		
		for (int i = 0; i < result_list.size(); i++) {  
			
			temp = result_list.get(i); 
			count = 0; 
			
			for (int j = 0; j < result_list.size(); j++) { 
				
				if (result_list.get(j) == temp) { 
					count++;
				}
			}
			
			if (count > max_count) { 
				max_count = count;
				max_label = temp;
			}
		}
		
		return max_label;
	}
	
	/** 
	 * Usually the string is cleaned as the class label is appended with the model number and ngram number
	 * for example fake_ngram_number_model_number
	 * @param s String containing the label to be sanitized
	 * @return String sans the appendages
	 */ 
	
	public static String sanitizeString(String s) { 
		
		String clean = s;
		int index = -1;
		
		index = s.indexOf('_');
		
		if (index != -1) { 
			
			clean = s.substring(0, index).trim();
		}
		
		return clean;
	} 
}
