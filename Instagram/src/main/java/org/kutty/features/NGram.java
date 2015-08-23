package org.kutty.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * Class for extracting the NGram features from an external file and writing it to an external file
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 August, 2015
 * 
 * TODO - Add Support For TagSet and Test For Null Pointer Exceptions
 * TODO - Check for Spam Support as well And Make things clear for Bigram and Trigrams
 */ 

public class NGram {
	
	/** 
	 * Defines the pipeline for extraction of NGram features from the given dataset
	 * @param filename String containing the filename containing the data
	 * @param n Integer containing the value of N in Ngram
	 * @param channel String containing the channel name
	 * @param type String containing the type of data i.e. Sentiment or Giveaway
	 */ 
	
	public static void NGramExtractionPipeline(String filename,int n,String channel,String type) { 

		List<Post> post_list = new ArrayList<Post>();
		Map<String,Integer> ngram_count = new HashMap<String,Integer>();
		Map<String,Integer> label_count_map;
		Map<String,Integer> spam_count_map;
		FeatureUtil feat = new FeatureUtil();  
		  
		if (channel.equalsIgnoreCase("Instagram") && type.equalsIgnoreCase("giveaway")) { 

			FeatureUtil.populateInstagramGiveawayData(filename, post_list);
			label_count_map = feat.giveaway_count_map;
		}  

		else if (channel.equalsIgnoreCase("Instagram") && type.equalsIgnoreCase("sentiment")) { 

			FeatureUtil.populateInstagramSentimentData(filename, post_list);
			label_count_map = feat.sentiment_count_map;
			spam_count_map = feat.spam_count_map;

		} else { 

			FeatureUtil.populateOtherChannelData(filename, post_list);
			label_count_map = feat.sentiment_count_map;
			spam_count_map = feat.spam_count_map;
		} 
		
		label_count_map.putAll(LabelCountUtil.getGiveawayLabelCount(post_list)); 
		
		for (Post p : post_list) { 

			String content = p.getContent();
			content = content.toLowerCase().trim();
			content = FeatureUtil.cleanString(content);
			content = FeatureUtil.removeStopWords(content);
			content = FeatureUtil.getStemPerWord(content);
			content = FeatureUtil.getNGram(content, n);
			p.setContent(content);
			getNGramCount(p,post_list,ngram_count,"giveaway");	
			System.out.println(ngram_count);
		}	
	}

	/** 
	 * Returns the count of NGrams for a given NGram and a classification type
	 * @param p Post containing the post object along with the content and other things
	 * @param post_list List<Post> containing the posts for a given training set
	 * @param type Type of classification which needs to be done these include (Giveaway,Sentiment Analyis etc)
	 * @return Integer containing the count of integers
	 */ 

	public static void getNGramCount(Post p,List<Post> post_list,Map<String,Integer> ngram_map,String type) { 

		int count = 0;
		String sentence = p.getContent();
		String ngram = "";
		String ngram_label = "";
		int index = -1; 

		if (type.equalsIgnoreCase("giveaway")) { 

			ngram_label = p.getGiveawayLabel();
		}

		if (type.equalsIgnoreCase("sentiment")) { 

			ngram_label = p.getSentimentLabel();
		}

		if (type.equalsIgnoreCase("spam")) { 

			ngram_label = p.getSpamLabel();
		}

		index = sentence.indexOf('|');  
		
		while(index != -1) { 

			ngram = sentence.substring(0, index).trim();
			sentence = sentence.substring(index+1);
			index = sentence.indexOf('|');
			count = getNGramCountUtil(ngram, post_list, ngram_label, "giveaway");
			
			if (ngram_map.containsKey(ngram)) { 
				
				int temp = ngram_map.get(ngram);
				ngram_map.put(ngram,temp+count); 
				
			} else {  
				
				ngram_map.put(ngram,count);
			}
		}
	}
	
	/** 
	 * Given an NGram counts its occurrence in dataset
	 * @param ngram String containing the ngram which is to be counted
	 * @param post_list List<Post> containing the entire post list
	 * @param ngram_label String containing the NGram label count
	 * @param type String containing the type of analysis to be made
	 * @return Integer containing count of the NGram in a given post list
	 */ 
	
	public static int getNGramCountUtil(String ngram,List<Post> post_list, String ngram_label,String type) { 

		int count = 0;
		String sentence = ""; 
		String post_label = ""; 
		String spam_label = ""; 

		for (Post temp : post_list) { 

			sentence = temp.getContent();

			if (type.equalsIgnoreCase("giveaway")) {  

				post_label = temp.getGiveawayLabel();
			}

			else if (type.equalsIgnoreCase("sentiment")) { 

				post_label = temp.getSentimentLabel();
			}

			else if (type.equalsIgnoreCase("spam")) { 

				spam_label = temp.getSpamLabel();
			}

			if (spam_label != null && post_label != null && (!spam_label.isEmpty() || !post_label.isEmpty())) {

				if (ngram_label != null && (ngram_label.equalsIgnoreCase(post_label) || ngram_label.equalsIgnoreCase(spam_label))) {

					count = count + getSubStringCount(ngram, sentence);
				}
			}

			spam_label = "";
			post_label = "";
		}

		return count;
	}
	
	/** 
	 * Given an NGram and a sentence returns the count of the ngram in the sentence
	 * @param ngram String containing the ngram
	 * @param sentence String containing the sentence
	 * @return Integer containing the count of the NGram
	 */ 
	
	public static int getSubStringCount(String ngram,String sentence) {  

		int count = 0;
		int index = -1;

		index = sentence.indexOf(ngram);

		while(index != -1) { 

			count++;
			sentence = sentence.substring(index+ngram.length());
			index = sentence.indexOf(ngram);
		}

		return count;
	}
	
	/** 
	 * Main function to test the overall functionality of the class 
	 * @param args
	 */ 
	
	public static void main(String args[]) {  

		NGramExtractionPipeline("insta_test.txt",3,"Instagram","giveaway");
	}
}
