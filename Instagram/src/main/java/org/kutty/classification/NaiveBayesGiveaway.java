package org.kutty.classification;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kutty.features.FeatureUtil;

/** 
 * Naive Bayes Classifier for Instagram Giveaway detection 
 * 
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 25 August,2015
 * TODO - Fix the issue of map duplicates
 * 
 */ 

public class NaiveBayesGiveaway { 
	
	public Map <String, Double> real_map;
	public Map <String, Double> fake_map;
	public Map <String, Double> real_tag_map;
	public Map <String, Double> fake_tag_map; 
	public String filename; 
	
	public NaiveBayesGiveaway(String filename) { 
		
		real_map = new HashMap <String, Double>();
		fake_map = new HashMap <String, Double>();
		real_tag_map = new HashMap <String, Double>();
		fake_tag_map = new HashMap <String, Double>();
		this.filename = filename;
	}
	
	public String classifyGiveaway(String captionText,String tagset) { 
		
		String processCaption = preProcessingPipelineForContent(captionText);
		String processTagSet = preProcessingPipelineForTag(tagset);
		double [] caption_probability = new double[2];
		double [] tag_probability = new double[2]; 
		Map <String, Double> ngram_probabilty = new HashMap <String, Double>(); 
		Entry<String, Double> max_entry; 
		
		//TODO - File Loading Module 
		
		real_map = LoadModel.getTrainedModel("");
		fake_map = LoadModel.getTrainedModel("");
		real_tag_map = LoadModel.getTrainedModel("");
		fake_tag_map = LoadModel.getTrainedModel("");
		
		processTagSet = FeatureUtil.getNGram(processTagSet, 1);
		tag_probability[0] = getProbability(processTagSet, real_tag_map);
		tag_probability[1] = getProbability(processTagSet, fake_tag_map);
		
		for (int i = 1; i <= 3; i++) { 
			
			processCaption = FeatureUtil.getNGram(processCaption, i);
			caption_probability[0] = getProbability(processCaption, real_map)*tag_probability[0];
			caption_probability[1] = getProbability(processCaption, fake_map)*tag_probability[1];
			ngram_probabilty.putAll(getClassLabelAndConfidence(caption_probability));		
		} 
		
		max_entry = getMaxEntry(ngram_probabilty); 
		
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
	
	public static Map<String, Double> getClassLabelAndConfidence(double a[]) { 
		
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
			
			max_pair.put("real", max); 
			
		} else { 
			
			max_pair.put("fake", max);
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
}
