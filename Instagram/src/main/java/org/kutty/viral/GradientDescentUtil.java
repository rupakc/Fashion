package org.kutty.viral;

import java.util.Random;

/** 
 * Carries out weight adjustment using gradient descent algorithm
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 November,2015
 */
public class GradientDescentUtil {
	
	public static Double[] getAugmentedFeatureVector(Double feature[]) { 
		
		Double[] augmentedFeature = new Double[feature.length+1];
		augmentedFeature[0] = 1.0;  
		
		for (int i = 1; i < augmentedFeature.length; i++) { 
			
			augmentedFeature[i] = feature[i-1];
		}
		
		return augmentedFeature;
	}
	
	public static Double[] getInitialWeight(int dimension) { 
		
		Double initialWeight [] = new Double[dimension];
		Random random = new Random(System.currentTimeMillis()); 
		
		for (int i = 0; i < initialWeight.length; i++) { 
			
			initialWeight[i] = random.nextGaussian();
		}
		
		return initialWeight;
	}
	
	public static void main(String args[]) { 
		
		for (Double t : getInitialWeight(10)) { 
			System.out.println(t);
		}
	}
}
