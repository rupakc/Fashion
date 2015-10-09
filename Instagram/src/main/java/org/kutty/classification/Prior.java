package org.kutty.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kutty.constants.Constants;
import org.kutty.features.FeatureUtil;
import org.kutty.features.LabelCountUtil;
import org.kutty.features.Post;

/** 
 * Calculates the prior probabilities of the classes and provides methods to interact with the same
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 9 October,2015
 * TODO - Add retrieve from file functions, Define the pipeline
 */

public class Prior {

	/** 
	 * Given a Map of label counts returns the prior probability Map for a given label count map
	 * @param labelCountMap Map<String,Integer> containing the labels and their counts
	 * @return Map<String,Double> containing the mapping between the priors and the labels
	 */

	public static Map<String,Double> getPriorMap(Map<String,Integer> labelCountMap) { 

		double totalCount = 1.0; 
		double count; 
		Map<String,Double> labelPriorMap = new HashMap<String,Double>();

		for (Integer labelCount: labelCountMap.values()) {

			totalCount = totalCount + labelCount;
		}

		for (String label : labelCountMap.keySet()) { 

			count = labelCountMap.get(label);
			count = count/totalCount;
			labelPriorMap.put(label, count);
		}

		return labelPriorMap;
	}

	/** 
	 * Given a channel name and model number returns the label count map for sentiments
	 * @param channel String containing the channel name
	 * @param modelNumber Integer containing the model number
	 * @return Map<String,Integer> containing the mapping between the class label and its count
	 */

	public static Map<String,Integer> generateLabelSentimentCountMap(String channel,int modelNumber) { 

		channel = channel.toLowerCase().trim();
		String filename = channel + "/" + "split_" + modelNumber + ".txt";
		Map<String,Integer> labelCountMap;
		List<Post> postList = new ArrayList<Post>();

		if (channel.equalsIgnoreCase("Instagram")) {  

			FeatureUtil.populateInstagramSentimentData(filename, postList);
			labelCountMap = LabelCountUtil.getSentimentLabelCount(postList);
		} 

		else {  

			FeatureUtil.populateOtherChannelData(filename, postList);
			labelCountMap = LabelCountUtil.getSentimentLabelCount(postList);
		}

		return labelCountMap;
	} 

	/** 
	 * Given a channel name and model number returns the label count map for sentiments
	 * @param channel String containing the channel name
	 * @param modelNumber Integer containing the model number
	 * @return Map<String,Integer> containing the mapping between the class label and its count
	 */

	public static Map<String,Integer> generateLabelSpamCountMap(String channel,int modelNumber) { 

		channel = channel.toLowerCase().trim();
		String filename = channel + "/" + "split_" + modelNumber + ".txt";
		Map<String,Integer> labelCountMap;
		List<Post> postList = new ArrayList<Post>();

		if (channel.equalsIgnoreCase("Instagram")) {  

			FeatureUtil.populateInstagramSentimentData(filename, postList);
			labelCountMap = LabelCountUtil.getSpamLabelCount(postList);
		} 

		else {  

			FeatureUtil.populateOtherChannelData(filename, postList);
			labelCountMap = LabelCountUtil.getSpamLabelCount(postList);
		}

		return labelCountMap;
	} 

	/** 
	 * Generate Giveaway label count for Instagram
	 * @param modelNumber Integer containing the model number
	 * @return Map<String,Integer> containing the class label and their counts
	 */

	public static Map<String,Integer> generateLabelGiveawayCountMap(int modelNumber) { 

		String filename = Constants.GIVEAWAY_FOLDER + "/" + "split_" + modelNumber + ".txt";
		Map<String,Integer> labelCountMap;
		List<Post> postList = new ArrayList<Post>();

		FeatureUtil.populateInstagramGiveawayData(filename, postList);
		labelCountMap = LabelCountUtil.getGiveawayLabelCount(postList);

		return labelCountMap;

	}

	/** 
	 * Writes the sentiment prior probabilities to an external text file
	 * @param filename String containing the filename
	 * @param priorMap Map<String,Double> containing the prior probability map
	 * @param modelNumber Integer containing the model number
	 * @throws IOException
	 */

	public static void writeSentimentPriorsToFile(String filename,Map<String,Double> priorMap,int modelNumber) throws IOException { 

		BufferedWriter bw;
		FileWriter fw;
		File f = new File(filename);
		String listPrior;

		if (!f.exists()) { 
			f.createNewFile();
		}

		fw = new FileWriter(f,true);
		bw = new BufferedWriter(fw); //Format of file is : modelNo = positive,negative,neutral

		listPrior = priorMap.get(Constants.POSITIVE_LABEL) + "," + priorMap.get(Constants.NEGATIVE_LABEL) 
				+ "," + priorMap.get(Constants.NEUTRAL_LABEL);
		listPrior = modelNumber + "=" + listPrior;
		bw.write(listPrior);
		bw.newLine();

		bw.close();
		fw.close();

	}

	/** 
	 * Writes the spam priors to a given external file
	 * @param filename String containing the filename
	 * @param priorMap Map<String,Double> containing the prior probability map
	 * @param modelNumber Integer containing the modelNumber
	 * @throws IOException
	 */

	public static void writeSpamPriorsToFile(String filename,Map<String,Double> priorMap,int modelNumber) throws IOException { 

		BufferedWriter bw;
		FileWriter fw;
		File f = new File(filename);
		String listPrior;

		if (!f.exists()) { 
			f.createNewFile();
		}

		fw = new FileWriter(f,true);
		bw = new BufferedWriter(fw); //Format of file is : modelNo = spam,ham

		listPrior = priorMap.get(Constants.SPAM_LABEL) + "," + priorMap.get(Constants.HAM_LABEL); 
		listPrior = modelNumber + "=" + listPrior;
		bw.write(listPrior);
		bw.newLine();

		bw.close();
		fw.close();

	}

	/** 
	 * Writes the Giveaway priors to a given file 
	 * @param filename String containing the filename
	 * @param priorMap Map<String,Double> containing the prior probability Map
	 * @param modelNumber Integer containing the model number
	 * @throws IOException
	 */

	public static void writeGiveawayPriorsToFile(String filename,Map<String,Double> priorMap,int modelNumber) throws IOException { 

		BufferedWriter bw;
		FileWriter fw;
		File f = new File(filename);
		String listPrior;

		if (!f.exists()) { 
			f.createNewFile();
		}

		fw = new FileWriter(f,true);
		bw = new BufferedWriter(fw); //Format of file is : modelNo = real,fake

		listPrior = priorMap.get(Constants.REAL_LABEL) + "," + priorMap.get(Constants.FAKE_LABEL); 
		listPrior = modelNumber + "=" + listPrior;
		bw.write(listPrior);
		bw.newLine();

		bw.close();
		fw.close();

	}

	public static Map<String,Double> getSentimentLabelPriors(String filename,int modelNumber) throws IOException { 

		BufferedReader br;
		FileReader fr;
		File f;
		Map<String,Double> sentimentLabelMap;
		String line;
		int model;
		int index;

		f = new File(filename); 
		index = -1;
		sentimentLabelMap = new HashMap<String,Double>(); 

		if (f.exists()) {  

			fr = new FileReader(f);
			br = new BufferedReader(fr); 

			while((line = br.readLine())!=null) { 
				index = line.indexOf('=');
				if(index != -1) { 
					model = Integer.valueOf(line.substring(0, index).trim());
					if (model == modelNumber) { 
						String [] labels = line.substring(index+1).trim().split(",");
						sentimentLabelMap.put(Constants.POSITIVE_LABEL,Double.valueOf(labels[0]));
						sentimentLabelMap.put(Constants.NEGATIVE_LABEL,Double.valueOf(labels[1]));
						sentimentLabelMap.put(Constants.NEUTRAL_LABEL,Double.valueOf(labels[2]));
						break;
					}
				}
			}

			br.close();
			fr.close();
		}

		return sentimentLabelMap;
	}
	
	/** 
	 * 
	 * @param filename
	 * @param modelNumber
	 * @return
	 * @throws IOException
	 */
	
	public static Map<String,Double> getSpamLabelPriors(String filename,int modelNumber) throws IOException { 

		BufferedReader br;
		FileReader fr;
		File f;
		Map<String,Double> spamLabelMap;
		String line;
		int model;
		int index;

		f = new File(filename); 
		index = -1;
		spamLabelMap = new HashMap<String,Double>(); 

		if (f.exists()) {  

			fr = new FileReader(f);
			br = new BufferedReader(fr); 

			while((line = br.readLine())!=null) { 
				index = line.indexOf('=');
				if(index != -1) { 
					model = Integer.valueOf(line.substring(0, index).trim());
					if (model == modelNumber) { 
						String [] labels = line.substring(index+1).trim().split(",");
						spamLabelMap.put(Constants.SPAM_LABEL,Double.valueOf(labels[0]));
						spamLabelMap.put(Constants.HAM_LABEL,Double.valueOf(labels[1]));
						break;
					}
				}
			}

			br.close();
			fr.close();
		}

		return spamLabelMap;
	}
	
	/** 
	 * 
	 * @param filename
	 * @param modelNumber
	 * @return
	 * @throws IOException
	 */
	
	public static Map<String,Double> getGiveawayLabelPriors(String filename,int modelNumber) throws IOException { 

		BufferedReader br;
		FileReader fr;
		File f;
		Map<String,Double> giveawayLabelMap;
		String line;
		int model;
		int index;

		f = new File(filename); 
		index = -1;
		giveawayLabelMap = new HashMap<String,Double>(); 

		if (f.exists()) {  

			fr = new FileReader(f);
			br = new BufferedReader(fr); 

			while((line = br.readLine())!=null) { 
				index = line.indexOf('=');
				if(index != -1) { 
					model = Integer.valueOf(line.substring(0, index).trim());
					if (model == modelNumber) { 
						String [] labels = line.substring(index+1).trim().split(",");
						giveawayLabelMap.put(Constants.REAL_LABEL,Double.valueOf(labels[0]));
						giveawayLabelMap.put(Constants.FAKE_LABEL,Double.valueOf(labels[1]));
						break;
					}
				}
			}

			br.close();
			fr.close();
		}

		return giveawayLabelMap;
	}

	public static void main(String args[]) throws IOException { 

		Map<String, Integer> labelCountMap = new HashMap<String,Integer>();
		labelCountMap.put("positive", 100);
		labelCountMap.put("negative",500);
		labelCountMap.put("neutral", 400);
		writeSentimentPriorsToFile("twitter/prior.txt",getPriorMap(labelCountMap),0);
		writeSpamPriorsToFile("twitter/prior.txt",getPriorMap(generateLabelSpamCountMap("twitter",5)),5);
		System.out.println(getPriorMap(generateLabelGiveawayCountMap(3)));

	}
}
