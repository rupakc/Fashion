package org.kutty.viral;

import java.util.List;
import java.util.Random;

import org.kutty.dbo.Feature;
import org.kutty.utils.MatrixUtils;

/** 
 * Carries out weight adjustment using gradient descent algorithm
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 13 November,2015
 */
public class GradientDescentUtil {

	/** 
	 * Augments the feature vector by adding one to the feature vector
	 * @param feature Array of double containing feature vector
	 * @return Double vector containing the augmented vector
	 */
	public static Double[] getAugmentedFeatureVector(Double feature[]) { 

		Double[] augmentedFeature = new Double[feature.length+1];
		augmentedFeature[0] = 1.0;  

		for (int i = 1; i < augmentedFeature.length; i++) { 

			augmentedFeature[i] = feature[i-1];
		}

		return augmentedFeature;
	}

	/** 
	 * Initializes the Initial weight vector to random values 
	 * @param dimension Integer containing the dimension of the vector
	 * @return Double[] containing the initial weight configuration
	 */
	public static Double[] getInitialWeight(int dimension) { 

		Double initialWeight [] = new Double[dimension];
		Random random = new Random(System.currentTimeMillis()); 

		for (int i = 0; i < initialWeight.length; i++) { 

			initialWeight[i] = random.nextGaussian();
		}

		return initialWeight;
	}
	
	/** 
	 * Trains the logistic regression model to adjust the weights
	 * @param trainSet List<Feature> containing the feature set
	 * @return Double[] containing the final weight vector
	 */
	public static Double[] getModelWeights(List<Feature> trainSet) { 

		Double[] weights = getInitialWeight(trainSet.get(0).getDimension());
		Double predictedValue;
		int predictedClassLabel;
		int differenceLabel;
		Double learningRate = 0.1;
		Double adjustmentFactor;
		Double hadamardProduct; 
		Double [] trainFeature;
		Double [] updateVector; 
		Double errorThreshold = 0.01;
		int maxIterations = 100; 
		int count = 0; 

		while(count < maxIterations) { 

			for (int i = 0; i < trainSet.size(); i++) { 

				trainFeature = trainSet.get(i).getFeatureVector();
				hadamardProduct = MatrixUtils.getHadamardProduct(trainFeature,weights);
				predictedValue = 1 + Math.exp(-1.0*hadamardProduct);
				predictedValue = 1/predictedValue;

				if (predictedValue >= 0.5) { 

					predictedClassLabel = 1; 

				} else { 

					predictedClassLabel = 0;
				}

				differenceLabel = (predictedClassLabel - trainSet.get(i).getClassLabel());
				adjustmentFactor = learningRate*differenceLabel;
				updateVector = MatrixUtils.multiplyByScalar(trainFeature, adjustmentFactor);
				weights = MatrixUtils.subtractVectors(weights,updateVector);

			} 
			
			count++;
		}

		return weights;
	}
}
