package org.kutty.classification;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/** 
 * Generic Class for a single layer perceptron, it carries out the 
 * perceptron learning using standard learning rule 
 *  
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 26 August, 2015
 * 
 */ 

public class Perceptron {

	public int input_nodes;
	public double [] weight_vector;
	public String transfer_function;
	public double bias; 
	public double learning_rate; 
	public double threshold; 
	public int max_iterations; 

	/** 
	 * public constructor to initialize the perceptron with number of input nodes
	 * @param input_nodes Integer specifying the number of input nodes
	 */ 

	public Perceptron(int input_nodes) { 

		this.input_nodes = input_nodes;
		weight_vector = new double[this.input_nodes];
		bias = Math.random(); 
		learning_rate = 0.1; 
		threshold = 0.001; 
		max_iterations = 100; 

		for (int i = 0; i < this.input_nodes; i++) { 

			weight_vector[i] = (Math.random() - 0.5);
		}
	}

	/** 
	 * public constructor to initialize the number of input nodes and the transfer function 
	 * @param input_nodes Integer representing the number of input nodes
	 * @param transfer_function String associating with the transfer function
	 */ 

	public Perceptron(int input_nodes,String transfer_function) { 

		this.input_nodes = input_nodes;
		weight_vector = new double[this.input_nodes];
		this.transfer_function = transfer_function; 
		bias = Math.random(); 
		learning_rate = 0.1; 
		threshold = 0.01;  
		max_iterations = 100;  

		for (int i = 0; i < this.input_nodes; i++) { 

			weight_vector[i] = (Math.random() - 0.5);
		}
	}

	/** 
	 * public constructor to initialize the perceptron weights, and learning rate
	 * @param input_nodes Integer representing the number of input nodes
	 * @param learning_rate Double containing the learning rate of the perceptron
	 */ 

	public Perceptron(int input_nodes,double learning_rate) { 

		this.input_nodes = input_nodes;
		weight_vector = new double[this.input_nodes];
		bias = Math.random(); 
		this.learning_rate = learning_rate; 
		threshold = 0.01; 
		max_iterations = 100;  

		for (int i = 0; i < this.input_nodes; i++) { 

			weight_vector[i] = (Math.random() - 0.5);
		}
	}

	/** 
	 * Calculates the weighted sum of the neuron
	 * @param input_vector input vector for the single layer neuron
	 * @return the weighted sum of the input plus the bias
	 */ 

	public double getSum(double input_vector[]) { 

		double sum = 0.0;

		for (int i = 0; i < input_vector.length; i++) { 

			sum = sum + input_vector[i]*weight_vector[i];
		}

		sum = sum + bias; 

		return sum;
	}

	/** 
	 * Updates the weights of the perceptron based on the error
	 * @param expected_output the expected output of the given perceptron
	 * @param actual_output the actual output of the given perceptron
	 * @param input_vector input vector which is needed to update the weights
	 * @return Array containing the set of updated weights
	 */ 

	public double[] updateWeights(double expected_output,double actual_output,double [] input_vector) { 

		double delta = learning_rate*(expected_output-actual_output);
		double [] new_weights = new double[this.input_nodes];

		for (int i = 0; i < new_weights.length; i++) { 

			new_weights[i] = weight_vector[i] + delta*input_vector[i];
		}

		return new_weights;
	}

	/** 
	 * Calculates the error between the old and new set of weights 
	 * @param old_weights Array containing the set of old weights
	 * @param new_weights Array containing the set of new weights
	 * @return Floating point number containing the error between the two weights
	 */ 

	public double getError(double [] old_weights, double [] new_weights) { 

		double error = 0.0; 

		for (int i = 0; i < old_weights.length; i++) {  

			error = error + Math.abs(new_weights[i]-old_weights[i]);
		}

		error = (error/new_weights.length);

		return error;
	} 

	/** 
	 * Trains a given perceptron for a class label and a given input vector
	 * @param input_vector input vector which is to be converted to its word space representation
	 * @param expected_class_label Integer representing the expected class label
	 * @throws IOException
	 */ 

	public void trainPerceptron(double [] input_vector,int expected_class_label,String filename) throws IOException { 

		double error = Double.MAX_VALUE;
		double sum = 0.0; 
		double [] new_weight_vector = null; 
		int actual_class_label; 
		int iteration = 0; 

		while(error > threshold && iteration < max_iterations) { 

			sum = getSum(input_vector);

			if (sum > 0) { 

				actual_class_label = 1; 

			} else {  

				actual_class_label = 0;
			}

			new_weight_vector = updateWeights(expected_class_label,actual_class_label,input_vector);

			error = getError(this.weight_vector,new_weight_vector);
			this.weight_vector = new_weight_vector; 

			iteration++;
		}

		writeVecToFile(filename,new_weight_vector);
	}

	/** 
	 * 
	 * @param input_vector
	 * @param expected_class_label
	 * @return
	 */ 

	public double[] trainPerceptron(double [] input_vector,int expected_class_label) { 

		double error = Double.MAX_VALUE;
		double sum = 0.0; 
		double [] new_weight_vector = null; 
		int actual_class_label; 
		int iteration = 0; 

		while(error > threshold && iteration < max_iterations) { 

			sum = getSum(input_vector);

			if (sum > 0) { 

				actual_class_label = 1; 

			} else {  

				actual_class_label = 0;
			}

			new_weight_vector = updateWeights(expected_class_label,actual_class_label,input_vector);

			error = getError(this.weight_vector,new_weight_vector);
			this.weight_vector = new_weight_vector; 

			iteration++;
		}

		return new_weight_vector;
	}  

	/** 
	 * Trains a perceptron using the pocket algorithm to find the best set of weights
	 * @param input_vector input vector which is to be used for training the perceptron
	 * @param expected_class_label Integer representing the expected class label of the input vector
	 * @throws IOException
	 */ 

	public void trainPerceptronPocket(double [] input_vector,int expected_class_label,String filename) throws IOException { 

		double error = Double.MAX_VALUE;
		double sum = 0.0; 
		double [] new_weight_vector = null; 
		int actual_class_label; 
		int iteration = 0; 
		double min_error = Double.MAX_VALUE;
		double best_weight_vector[] = null; 

		while(error > threshold && iteration < max_iterations) { 

			sum = getSum(input_vector);

			if (sum > 0) { 

				actual_class_label = 1; 

			} else {  

				actual_class_label = 0;
			}

			new_weight_vector = updateWeights(expected_class_label,actual_class_label,input_vector);

			error = getError(this.weight_vector,new_weight_vector);
			this.weight_vector = new_weight_vector;
			iteration++;

			if (error < min_error) {  

				min_error = error;
				best_weight_vector = new_weight_vector;
			}
		}

		writeVecToFile(filename,best_weight_vector);
	}

	/** 
	 * Writes the given word vector to a file
	 * @param filename String which contains the name of the file to which it is to be written
	 * @param vector Array containing the frequency of word counts
	 * @throws IOException
	 */ 

	public void writeVecToFile(String filename,double [] vector)throws IOException { 

		BufferedWriter bw;
		FileWriter fw;
		String feature = ""; 

		fw = new FileWriter(filename,true);
		bw = new BufferedWriter(fw);

		for (int i = 0; i < vector.length-1; i++) { 

			feature = feature + String.valueOf(vector[i]) + ",";
		}

		feature = feature + String.valueOf(vector[vector.length-1]);

		bw.write(feature);
		bw.newLine(); 

		bw.close();
		fw.close();
	}
}
