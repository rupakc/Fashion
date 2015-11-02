package org.kutty.benchmark;

import java.util.List;

import org.kutty.dbo.Benchmark;

/** 
 * Defines a set of utility functions for testing model performance
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 11 October, 2015
 *
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
}
