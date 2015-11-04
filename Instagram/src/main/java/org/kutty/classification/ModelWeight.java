package org.kutty.classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** 
 * Calculates the accuracy of each model and saves it for later use
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 3 November,2015
 */

public class ModelWeight {
	
	public int modelNumber;
	public String channelName;
	public String modelFilePath;
	
	public ModelWeight(int modelNumber,String channelName) {  
		
		this.modelNumber = modelNumber;
		this.channelName = channelName;
		this.modelFilePath = this.channelName.toLowerCase().trim() + "/" + "model_weights.txt";
	}
	
	public Double getModelWeight()throws IOException { // Format of the file is ModelNum=Right,Wrong
		
		String temp = "";
		BufferedReader br;
		FileReader fr;
		double weight = 0.0;
		int index = -1;
		int model;
		
		fr = new FileReader(this.modelFilePath);
		br = new BufferedReader(fr);
		
		while((temp = br.readLine()) != null) { 
			
			index = temp.indexOf("=");
			if(index != -1) { 
				model = Integer.valueOf(temp.substring(0, index).trim());
				if (model == this.modelNumber) { 
					String [] weightArray = temp.substring(index+1).trim().split(",");
					int correct = Integer.valueOf(weightArray[0]);
					int wrong = Integer.valueOf(weightArray[1]);
					weight = (correct/(wrong+1));
					weight = Math.exp(weight);
				}
			}
		}
		
		br.close();
		fr.close();
		
		return weight;
	}
}
