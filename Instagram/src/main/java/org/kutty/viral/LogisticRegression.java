package org.kutty.viral;

import java.util.HashMap;
import java.util.Map;


/** 
 * Classifies a post as viral or non viral using logistic regression 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 14 November,2015
 */
public class LogisticRegression {
	
	public static Map<String,Double> getViralLabel(String channel,String post) { 
		
		Map<String,Double> classLabel = new HashMap<>();
		Double weights[] = getModelParam(channel);
		
		return classLabel;
		
	}
	
	//TODO - Write logic to get weights for trained model
	private static Double[] getModelParam(String channel) {
		
		return null;
	}
}
