package org.kutty.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kutty.features.FeatureUtil;
import org.kutty.utils.ClassificationUtils;

/** 
 * Defines the class for spam detection across all channels
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 29 August, 2015
 * 
 */

public class NaiveBayesSpam {

	public Map <String, Double> spam_map;
	public Map <String, Double> ham_map;
	public Map <String, Double> spam_tag_map;
	public Map <String, Double> ham_tag_map; 
	public String SPAM_FILENAME = "/spam_";
	public String HAM_FILENAME = "/ham_";
	public String SPAM_TAG_FILENAME = "/tag_spam_";
	public String HAM_TAG_FILENAME = "/tag_ham_";
	public String CHANNEL_NAME = "";
	public int MODEL_NUMBER = 1;
	public int NGRAM_NUMBER;
	public String CLASS_LABEL;
	public double CLASS_PROB; 
	public Double MODEL_WEIGHT;
	
	/** 
	 * public constructor to initialize the model number and channel name for spam detection
	 * @param model_number Integer containing the model number
	 * @param channel_name String containing the channel name
	 */ 

	public NaiveBayesSpam(int model_number,String channel_name) { 

		this.MODEL_NUMBER = model_number;
		this.CHANNEL_NAME = channel_name.toLowerCase().trim();
		SPAM_FILENAME = this.CHANNEL_NAME + this.SPAM_FILENAME + this.MODEL_NUMBER + ".txt";
		HAM_FILENAME = this.CHANNEL_NAME + this.HAM_FILENAME + this.MODEL_NUMBER + ".txt"; 
		ModelWeight modelWeight = new ModelWeight(MODEL_NUMBER,this.CHANNEL_NAME);
		
		/*try { 
			this.MODEL_WEIGHT = modelWeight.getModelWeight();
		} catch (IOException e) {
			e.printStackTrace();
		} */

		if (CHANNEL_NAME.equalsIgnoreCase("instagram")) { 
			
			SPAM_TAG_FILENAME = this.CHANNEL_NAME + this.SPAM_TAG_FILENAME + this.MODEL_NUMBER + ".txt";
			HAM_TAG_FILENAME = this.CHANNEL_NAME + this.HAM_TAG_FILENAME + this.MODEL_NUMBER + ".txt";
		} 
		
		spam_map = LoadModel.getTrainedModel(SPAM_FILENAME);
		ham_map = LoadModel.getTrainedModel(HAM_FILENAME); 
		
		if (CHANNEL_NAME.equalsIgnoreCase("instagram")) { 
			
			spam_tag_map = LoadModel.getTrainedModel(SPAM_TAG_FILENAME);
			ham_tag_map = LoadModel.getTrainedModel(HAM_TAG_FILENAME);
		}
	}

	/** 
	 * Given a Instagram post classifies it as spam/ham
	 * @param captionText String containing the captionText to be classified
	 * @param tagset String containing the tagset to be classified
	 * @return String containing the spam label
	 */ 

	public String classifySpamInstagram(String captionText,String tagset) { 

		String processCaption = preProcessingPipelineForContent(captionText);
		String processTagSet = preProcessingPipelineForTag(tagset);
		double [] caption_probability = new double[2];
		double [] tag_probability = new double[2]; 
		Map <String, Double> ngram_probabilty = new HashMap <String, Double>(); 
		Entry<String, Double> max_entry; 

		processTagSet = FeatureUtil.getNGram(processTagSet, 1);
		tag_probability[0] = getProbability(processTagSet, spam_tag_map);
		tag_probability[1] = getProbability(processTagSet, ham_tag_map);

		for (int i = 1; i <= 3; i++) { 

			processCaption = FeatureUtil.getNGram(processCaption, i);
			caption_probability[0] = getProbability(processCaption, spam_map)*tag_probability[0];
			caption_probability[1] = getProbability(processCaption, ham_map)*tag_probability[1];
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
	 * Given a text from channels other than Instagram determines their spamicity
	 * @param text String containing the content which is to be analyzed
	 * @return String containing the class label (spam or ham)
	 */ 
	
	public String classifySpamOtherChannels(String text) { 

		String processText = preProcessingPipelineForContent(text);
		double [] content_probability = new double[2];
		Map <String, Double> ngram_probabilty = new HashMap <String, Double>(); 
		Entry<String, Double> max_entry; 

		for (int i = 1; i <= 3; i++) { 

			processText = FeatureUtil.getNGram(processText, i);
			content_probability[0] = getProbability(processText, spam_map);
			content_probability[1] = getProbability(processText, ham_map);
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
	 * Given the probabilities of being in a given class returns the most plausible class label
	 * @param a Double array containing the probabilities of the class labels
	 * @param ngram_model Integer containing the ngram model number
	 * @return Map <String, Double> containing the class label appended with ngram_model and model number
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

			max_pair.put("spam_" + ngram_model + "_" + this.MODEL_NUMBER, max); 

		} else { 

			max_pair.put("ham_" + ngram_model + "_" + this.MODEL_NUMBER, max);
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
	 * Returns the probability of a given label for a given class
	 * @param tagset String containing the text which is to be evaluated
	 * @param ngram_map Map <String, Double> containing the class label and its probability
	 * @return Double containing the probability of the class label
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

		
	}

}
