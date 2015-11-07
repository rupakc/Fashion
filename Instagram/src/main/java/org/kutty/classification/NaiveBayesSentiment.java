package org.kutty.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kutty.features.FeatureUtil;
import org.kutty.utils.ClassificationUtils;

/** 
 * Defines the pipeline for sentiment analysis of posts from all channels
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 29 August, 2015
 * 
 */ 

public class NaiveBayesSentiment {

	public Map <String, Double> positive_map;
	public Map <String, Double> negative_map;
	public Map <String, Double> neutral_map;
	public Map <String, Double> positive_tag_map;
	public Map <String, Double> negative_tag_map; 
	public Map <String, Double> neutral_tag_map; 
	public String POSITIVE_FILENAME = "/positive_";
	public String NEGATIVE_FILENAME = "/negative_";
	public String NEUTRAL_FILENAME = "/neutral_";
	public String POSITIVE_TAG_FILENAME = "/tag_positive_";
	public String NEGATIVE_TAG_FILENAME = "/tag_negative_";
	public String NEUTRAL_TAG_FILENAME = "/tag_neutral_";
	public String CHANNEL_NAME = "twitter";
	public int MODEL_NUMBER = 1;
	public int NGRAM_NUMBER;
	public String CLASS_LABEL;
	public double CLASS_PROB;
	public double MODEL_WEIGHT; 

	/** 
	 * public constructor to initialize the model number and the channel name
	 * @param model_number Integer containing the model number
	 * @param channel_name String containing the channel name
	 */ 

	public NaiveBayesSentiment(int model_number,String channel_name) { 

		this.MODEL_NUMBER = model_number;
		this.CHANNEL_NAME = channel_name.toLowerCase().trim();
		POSITIVE_FILENAME = this.CHANNEL_NAME + this.POSITIVE_FILENAME + this.MODEL_NUMBER + ".txt";
		NEGATIVE_FILENAME = this.CHANNEL_NAME + this.NEGATIVE_FILENAME + this.MODEL_NUMBER + ".txt";
		NEUTRAL_FILENAME = this.CHANNEL_NAME + this.NEUTRAL_FILENAME + this.MODEL_NUMBER + ".txt"; 
		//ModelWeight modelWeight = new ModelWeight(MODEL_NUMBER,this.CHANNEL_NAME);

		/*try { 
			this.MODEL_WEIGHT = modelWeight.getModelWeight();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		if (CHANNEL_NAME.equalsIgnoreCase("instagram")) { 

			POSITIVE_TAG_FILENAME = this.CHANNEL_NAME + this.POSITIVE_TAG_FILENAME + this.MODEL_NUMBER + ".txt";
			NEGATIVE_TAG_FILENAME = this.CHANNEL_NAME + this.NEGATIVE_TAG_FILENAME + this.MODEL_NUMBER + ".txt";
			NEUTRAL_TAG_FILENAME = this.CHANNEL_NAME + this.NEUTRAL_TAG_FILENAME + this.MODEL_NUMBER + ".txt";
		}

		positive_map = LoadModel.getTrainedModel(POSITIVE_FILENAME);
		negative_map = LoadModel.getTrainedModel(NEGATIVE_FILENAME);
		neutral_map = LoadModel.getTrainedModel(NEUTRAL_FILENAME); 

		if (CHANNEL_NAME.equalsIgnoreCase("instagram")) { 

			positive_tag_map = LoadModel.getTrainedModel(POSITIVE_TAG_FILENAME);
			negative_tag_map = LoadModel.getTrainedModel(NEGATIVE_TAG_FILENAME);
			neutral_tag_map = LoadModel.getTrainedModel(NEUTRAL_TAG_FILENAME);
		}
	}

	/** 
	 * Given a caption text and the associated tagset determines the sentiment
	 * @param captionText String containing the captionText
	 * @param tagset String containing the tagSet
	 * @return String containing the sentiment label (i.e. positive, negative or neutral)
	 */ 

	public String classifySentimentInstagram(String captionText,String tagset) { 

		String processCaption = preProcessingPipelineForContent(captionText);
		String processTagSet = preProcessingPipelineForTag(tagset);
		double [] caption_probability = new double[3];
		double [] tag_probability = new double[3]; 
		Map <String, Double> ngram_probabilty = new HashMap <String, Double>(); 
		Entry<String, Double> max_entry; 

		processTagSet = FeatureUtil.getNGram(processTagSet, 1);
		tag_probability[0] = getProbability(processTagSet, positive_tag_map);
		tag_probability[1] = getProbability(processTagSet, negative_tag_map);
		tag_probability[2] = getProbability(processTagSet, neutral_tag_map);

		for (int i = 1; i <= 3; i++) { 

			processCaption = FeatureUtil.getNGram(processCaption, i);
			caption_probability[0] = getProbability(processCaption, positive_map)*tag_probability[0];
			caption_probability[1] = getProbability(processCaption, negative_map)*tag_probability[1];
			caption_probability[2] = getProbability(processCaption, neutral_map)*tag_probability[2];
			ClassificationUtils.convertToPercentage(caption_probability);
			ngram_probabilty.putAll(getClassLabelAndConfidence(caption_probability,i));
		} 

		max_entry = getMaxEntry(ngram_probabilty);
		CLASS_LABEL = max_entry.getKey();
		CLASS_PROB = max_entry.getValue();
		NGRAM_NUMBER = ClassificationUtils.getNGramNumber(max_entry); 

		return max_entry.getKey();
	} 

	/** 
	 * Given contents from other channels classifies them into a particular sentiment
	 * @param text String containing the content
	 * @return String containing the sentiment label (i.e. positive, negative or neutral)
	 */ 

	public String classifySentimentOtherChannels(String text) { 

		String processText = preProcessingPipelineForContent(text);

		double [] content_probability = new double[3]; 
		Map <String, Double> ngram_probabilty = new HashMap <String, Double>(); 
		Entry<String, Double> max_entry; 

		for (int i = 1; i <= 3; i++) { 

			processText = FeatureUtil.getNGram(processText, i);
			content_probability[0] = getProbability(processText, positive_map);
			content_probability[1] = getProbability(processText, negative_map);
			content_probability[2] = getProbability(processText, neutral_map);
			ClassificationUtils.convertToPercentage(content_probability);
			ngram_probabilty.putAll(getClassLabelAndConfidence(content_probability,i));
		} 

		max_entry = getMaxEntry(ngram_probabilty);
		CLASS_LABEL = max_entry.getKey();
		CLASS_PROB = max_entry.getValue();
		NGRAM_NUMBER = ClassificationUtils.getNGramNumber(max_entry); 

		return max_entry.getKey();
	} 


	/** 
	 * Returns the map entry which has the maximum value
	 * @param ngram_output HashMap<String,Double> containing the value of the scores and labels for each class
	 * @return Entry<String,Double> which has the maximum value
	 */ 

	public static Entry<String,Double> getMaxEntry(Map<String,Double> ngram_output) { 

		Entry <String,Double> maxentry = null;

		for(Map.Entry<String, Double> temp: ngram_output.entrySet()) { 

			if (maxentry == null || temp.getValue() > maxentry.getValue()) {  

				maxentry = temp;
			}
		}

		return maxentry;
	}

	/** 
	 * For a given model returns the most probable class along with model_number and ngram model
	 * @param a  Array containing the class probabilities
	 * @param ngram_model Integer containing the ngram language model number (i.e. 1,2,3)
	 * @return Map <String, Double> containing the class label and its associated probability
	 */ 

	public Map<String, Double> getClassLabelAndConfidence(double a[],int ngram_model) { 

		double max = Double.MIN_VALUE; 
		int index = 0; 
		Map <String, Double> max_pair = new HashMap <String, Double>(); 

		for (int i = 0; i < a.length; i++) { 

			if (a[i] > max) { 

				max = a[i];
				index = i;
			}
		}

		if (index == 0) { 

			max_pair.put("positive_" + ngram_model + "_" + this.MODEL_NUMBER, max); 

		} else if (index == 1) { 

			max_pair.put("negative_" + ngram_model + "_" + this.MODEL_NUMBER, max); 

		} else if (index == 2) { 

			max_pair.put("neutral_" + ngram_model + "_" + this.MODEL_NUMBER, max);
		}

		return max_pair;
	}

	/** 
	 * Given a string cleans it up for necessary pre-processing
	 * @param content String containing the content to be pre-processed
	 * @return String which has been sanitized
	 */ 

	public static String preProcessingPipelineForContent(String content) { 

		content = content.toLowerCase().trim();
		content = FeatureUtil.cleanString(content);
		content = FeatureUtil.removeStopWords(content);
		content = FeatureUtil.getStemPerWord(content);

		return content;
	}

	/** 
	 * Given a tagset space separates it and removes the stopwords
	 * @param tagset String containing the tagset
	 * @param n Integer containing the value of N for Ngram
	 * @return String containing the cleaned and separated tagset
	 */ 

	public static String preProcessingPipelineForTag(String tagset) { 

		tagset = tagset.replace(","," ");
		tagset = tagset.toLowerCase();
		tagset = FeatureUtil.cleanString(tagset);
		tagset = FeatureUtil.removeStopWords(tagset);
		tagset = FeatureUtil.getStemPerWord(tagset);

		return tagset;
	}

	/** 
	 * Returns the probability of a given text in a given ngram language model
	 * @param tagset String containing the text to be classified
	 * @param ngram_map Map <String, Double> containing the ngram map
	 * @return double containing the probability value
	 */ 

	public double getProbability(String tagset,Map<String,Double> ngram_map) { 

		double count = 1.0;
		double temp_count; 

		int index = -1;
		String ngram;
		int previous_position = 0; 

		index = tagset.indexOf('|');

		while(index != -1) { 

			ngram = tagset.substring(previous_position, index);
			ngram = ngram.trim();
			ngram = getTransformedString(ngram); 

			if (ngram_map.containsKey(ngram) && ngram_map.get(ngram) <= 1.0) {  

				temp_count = ngram_map.get(ngram); 

			} else { 

				temp_count = 0.01;
			}

			count = count * temp_count;
			previous_position = index+1;
			index = tagset.indexOf('|',previous_position);
		} 

		return count;
	}

	/** 
	 * Given an ngram encloses it with a pair of braces
	 * @param ngram String containing the NGram
	 * @return String with the enclosing braces
	 */ 

	public static String getTransformedString(String ngram) { 

		String temp = ngram.trim();
		temp = "(" + ngram + ")";

		return temp;
	}
	
	/** 
	 * Main function to test the functionality of the given class
	 * @param args
	 */ 

	public static void main(String args[]) { 

		String text = "#Burberry & #TrueReligion What A Wonderful Combination!!! #JustAnotherDay #WaddupTwitter";
		System.out.println(new NaiveBayesSentiment(3,"facebook").classifySentimentOtherChannels(text));

	}

}
