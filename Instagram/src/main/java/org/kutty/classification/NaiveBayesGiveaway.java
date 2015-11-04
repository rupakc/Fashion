package org.kutty.classification;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kutty.features.FeatureUtil;
import org.kutty.utils.ClassificationUtils;

/** 
 * Naive Bayes Classifier for Instagram Giveaway detection 
 * 
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 25 August,2015
 * 
 */ 

public class NaiveBayesGiveaway { 
	
	public Map <String, Double> real_map;
	public Map <String, Double> fake_map;
	public Map <String, Double> real_tag_map;
	public Map <String, Double> fake_tag_map; 
	public String REAL_FILENAME = "giveaway/real_";
	public String FAKE_FILENAME = "giveaway/fake_";
	public String REAL_TAG_FILENAME = "giveaway/tag_real_";
	public String FAKE_TAG_FILENAME = "giveaway/tag_fake_";
	public int MODEL_NUMBER = 1;
	public int NGRAM_NUMBER;
	public String CLASS_LABEL;
	public Double CLASS_PROB;
	public Double MODEL_WEIGHT;
	
	/** 
	 * public constructor to initialize the model number for loading giveaways
	 * @param model_number Integer containing the model number
	 */ 
	
	public NaiveBayesGiveaway(int model_number) { 
		
		REAL_FILENAME = REAL_FILENAME + model_number + ".txt";
		FAKE_FILENAME = FAKE_FILENAME + model_number + ".txt";
		REAL_TAG_FILENAME = REAL_TAG_FILENAME +  model_number + ".txt";
		FAKE_TAG_FILENAME = FAKE_TAG_FILENAME + model_number + ".txt";
		this.MODEL_NUMBER = model_number;
		
		real_map = LoadModel.getTrainedModel(REAL_FILENAME);
		fake_map = LoadModel.getTrainedModel(FAKE_FILENAME);
		real_tag_map = LoadModel.getTrainedModel(REAL_TAG_FILENAME);
		fake_tag_map = LoadModel.getTrainedModel(FAKE_TAG_FILENAME);
	}
	
	/** 
	 * Given a captionText and Tagset returns the Giveaway class label
	 * @param captionText String containing the caption text
	 * @param tagset String containing the tagset
	 * @return String containing the Giveaway label (i.e. real or fake)
	 */ 
	
	public String classifyGiveaway(String captionText,String tagset) { 
		
		String processCaption = preProcessingPipelineForContent(captionText);
		String processTagSet = preProcessingPipelineForTag(tagset);
		double [] caption_probability = new double[2];
		double [] tag_probability = new double[2]; 
		Map <String, Double> ngram_probabilty = new HashMap <String, Double>(); 
		Entry<String, Double> max_entry; 
			
		processTagSet = FeatureUtil.getNGram(processTagSet, 1);
		tag_probability[0] = getProbability(processTagSet, real_tag_map);
		tag_probability[1] = getProbability(processTagSet, fake_tag_map);
		
		for (int i = 1; i <= 3; i++) { 
			
			processCaption = FeatureUtil.getNGram(processCaption, i);
			caption_probability[0] = getProbability(processCaption, real_map)*tag_probability[0];
			caption_probability[1] = getProbability(processCaption, fake_map)*tag_probability[1];
			ClassificationUtils.convertToPercentage(caption_probability);
			ngram_probabilty.putAll(getClassLabelAndConfidence(caption_probability,i));
		} 
		
		max_entry = getMaxEntry(ngram_probabilty); 
		NGRAM_NUMBER = ClassificationUtils.getNGramNumber(max_entry);
		CLASS_LABEL = max_entry.getKey();
		CLASS_PROB = max_entry.getValue();
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
	 * Returns a map of the most probable class label
	 * @param a Double array containing the probabilities
	 * @param ngram_model Integer containing the ngram model number
	 * @return Map <String, Double> containing the class label and its probability
	 */ 
	
	public Map <String, Double> getClassLabelAndConfidence(double a[],int ngram_model) { 
		
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
			
			max_pair.put("real_" + ngram_model + "_" + this.MODEL_NUMBER, max); 
			
		} else { 
			
			max_pair.put("fake_" + ngram_model + "_" + this.MODEL_NUMBER, max);
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
	 * Returns the probability of the given tagset and ngram map
	 * @param tagset String containing the text which has to be classified
	 * @param ngram_map Map <String, Double> containing the class label and its probability
	 * @return Double containing the probability of a given text for a class label
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
	 * Main function to test the functionality of the class
	 * @param args
	 */ 
	
	public static void main(String args[]) { 
		
		String captionText = ". Assalamualaikum semua.. ??????. . Alhamdulillah Lagi customer yang membeli iPhone dengan admin di kedai . ????????????. . Admin bukan Hanye menjual Iphone Sahaja tau. Semua model phone admin jual. Asus,Lenovo ,Nokia ,n etc. Yang mane nak tanye harga Boleh whatsapp admin or kita bincang di kedai admin ea. ?????????????. . TRADE IN ADMIN TERIMA !!. TRADE IN ADMIN TERIMA !!. . #buyerjusttekan yang beli dengan Harga #gempakdohhsale . ??????. . Anda bila lagi?? Heeeee . original iphone. -ready stock. -seal box. -prefer cod. -postage available. . Price list :. iPhone 4 16gb - rm 630 32gb - rm 660  iPhone 4s 16gb - rm 740 32gb - rm 780 64gb - rm 800  iPhone 5 16gb - rm 1070 32gb - rm 1100 64gb - rm 1150  iPhone 5s ( space grey ) 16gb - rm 1450 32gb - rm 1500 64gb - rm 1550  iPhone 5s ( silver ) 16gb - rm 1550 32gb - rm 1600 64gb - rm 1650  iPhone 5s ( gold ) 16gb - rm 1650 32gb - rm 1700 64gb - rm 1750  iPhone 6 16gb - rm 2450 64gb - rm 2650 128gb - rm 2750  iPhone 6 plus  16gb - rm 2950 64gb - rm 3150 128gb - rm 3450 . 017-7515417. Taufik  #iPhone #iphonemurah #iphonemurahmalaysia #iphoneoriginal #iphoneoriginalmalaysia #iphonesale #saleiphone #sale #repost #contest #freegift #giweaway #repostmalaysia #giveawaymalaysia #luckydraw #batupahat #johor #cod";
		String tagSet = "gift,giveawaymalaysia,pandorainspired,igshop,giveaway,katespademalaysia,norhanisabdaziz1stgiveaway,bazaarpaknil,hermesmalaysia,tagandwin,raya2015,giveawaycontest,readystock,igshopmalaysia,giveawaytime,repost";
		
		NaiveBayesGiveaway nb = new NaiveBayesGiveaway(5);
		System.out.println(nb.classifyGiveaway(captionText, tagSet));
	}
}
