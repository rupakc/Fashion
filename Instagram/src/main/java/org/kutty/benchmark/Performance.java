package org.kutty.benchmark;

import java.util.List;

import org.kutty.dbo.Benchmark;
import org.kutty.dbo.PerformanceMetrics;

/** 
 * Defines the performance pipeline for the classifiers to calculate different metrics
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 4 November, 2015
 */
public class Performance {
	
	/** 
	 * Returns a set of minimum performance metrics (i.e. Accuracy and Confusion Matrix)
	 * @param resultSet Lists<Benchmark> containing the test set
	 * @return PerformanceMetrics object containing the set of minimum performance metrics
	 */
	public static PerformanceMetrics getMinimumMetrics(List<Benchmark> resultSet) { 
		
		PerformanceMetrics perfMetrics = new PerformanceMetrics();
		
		perfMetrics.setAccuracy(PerformanceUtil.getAccuracy(resultSet));
		perfMetrics.setConfusionMatrix(PerformanceUtil.getConfusionMatrix(resultSet).getMatrix());
		
		return perfMetrics;
	}
	
	/** 
	 * Returns the set of all Micro performance metrics for a given test set
	 * @param resultSet List<Benchmark> containing the test set
	 * @return PerformanceMetrics containing the micro metrics
	 */
	public static PerformanceMetrics getMicroMetrics(List<Benchmark> resultSet) { 
		
		PerformanceMetrics perfMetrics = new PerformanceMetrics();
		
		perfMetrics.setAccuracy(PerformanceUtil.getAccuracy(resultSet));
		perfMetrics.setConfusionMatrix(PerformanceUtil.getConfusionMatrix(resultSet).getMatrix());
		perfMetrics.setMicroPrecision(PerformanceUtil.getMicroPrecision(resultSet));
		perfMetrics.setMicroRecall(PerformanceUtil.getMicroRecall(resultSet));
		perfMetrics.setMicroSpecificity(PerformanceUtil.getMicroSpecificity(resultSet));
		
		return perfMetrics;
	}
}
