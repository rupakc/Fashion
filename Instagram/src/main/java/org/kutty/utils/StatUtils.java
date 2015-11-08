package org.kutty.utils;

import java.util.Collections;
import java.util.List;

/** 
 * Defines Statistical Utilities commonly used
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 8 November,2015
 */
public class StatUtils {
	
	/** 
	 * Returns the median of a list of elements
	 * @param sortList List<Double> containing the elements
	 * @return Double containing the value of the median
	 */
	public static Double getMedian(List<Double> sortList) { 
		
		Collections.sort(sortList);
		int n = sortList.size();
		
		if (n == 0) { 
			
			return 0.0;
		}
		
		if (n == 1) { 
			
			return sortList.get(0);
		}
		
		if (n%2 != 0) { 
			
			return sortList.get(n/2);
		} 
		
		return ((sortList.get(n/2) + sortList.get(n/2 - 1))/2.0);
	}
	
	/** 
	 * Returns the mean of a given list of elements
	 * @param list List<Double> containing the list of numbers
	 * @return Double containing the mean of the elements
	 */
	public static Double getMean(List<Double> list) { 
		
		Double sum = 0.0;
		
		for (Double element : list) { 
			
			sum += element;
		}
		
		return (sum/list.size());
	}
	
	/** 
	 * 
	 * @param list
	 * @return
	 */
	public static Double getVariance(List<Double> list) { 
		
		Double var = 0.0;
		Double mean = getMean(list); 
		
		for (Double element : list) {  
			
			var += Math.pow((element-mean), 2.0);
		}
		
		return (var/list.size());
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Double getStandardDeviation(List<Double> list) { 
		
		Double std = getVariance(list);
		
		return Math.sqrt(std);
	}
}
