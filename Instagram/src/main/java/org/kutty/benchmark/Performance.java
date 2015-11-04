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
	
	public static PerformanceMetrics getMinimumMetrics(List<Benchmark> resultSet) { 
		
		PerformanceMetrics perfMetrics = new PerformanceMetrics();
		
		perfMetrics.setAccuracy(PerformanceUtil.getAccuracy(resultSet));
		perfMetrics.setConfusionMatrix(PerformanceUtil.getConfusionMatrix(resultSet).getMatrix());
		
		return perfMetrics;
	}
}
