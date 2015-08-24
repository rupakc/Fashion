package org.kutty.features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
 * TODO - Add Support For TagSet
 */ 

public class NGram {

	public Map<String,Integer> label_count_map;
	public Map<String,Integer> spam_count_map;
	public Map<String,Double> real_giveaway_count_map;
	public Map<String,Double> fake_giveaway_count_map;
	public Map<String,Double> positive_count_map;
	public Map<String,Double> negative_count_map;
	public Map<String,Double> neutral_count_map;
	public Map<String,Double> spam_map;
	public Map<String,Double> ham_map;

	/** 
	 * public constructor to initialize the different count maps
	 */ 

	public NGram() { 

		real_giveaway_count_map = new HashMap<String,Double>();
		fake_giveaway_count_map = new HashMap<String,Double>();
		positive_count_map = new HashMap<String,Double>();
		negative_count_map = new HashMap<String,Double>();
		neutral_count_map = new HashMap<String,Double>();
		spam_map = new HashMap<String,Double>();
		ham_map = new HashMap<String,Double>();
	} 

	/** 
	 * Defines the pipeline for extraction of NGram features from the given dataset
	 * @param filename String containing the filename containing the data
	 * @param n Integer containing the value of N in Ngram
	 * @param channel String containing the channel name
	 * @param type String containing the type of data i.e. Sentiment or Giveaway
	 */ 

	public void NGramExtractionPipeline(String filename,int n,String channel,String type) { 

		List<Post> post_list = new ArrayList<Post>();
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
			getNGramCountMapSelector(p,post_list,"giveaway");	
		}

		writeGramToFileSelector(type);
	}

	//TODO - Add file selector to write to the correct file
	
	/** 
	 * 
	 * @param type
	 */ 
	
	public void writeGramToFileSelector(String type) { 

		if (type.equalsIgnoreCase("giveaway")) { 

			writeGramToFile("giveaway/real_1.txt", real_giveaway_count_map);
			writeGramToFile("giveaway/fake_1.txt", fake_giveaway_count_map);
		}

		if (type.equalsIgnoreCase("sentiment")) { 

			writeGramToFile("",positive_count_map);
			writeGramToFile("",negative_count_map);
			writeGramToFile("",neutral_count_map);
		}

		if (type.equalsIgnoreCase("spam")) { 

			writeGramToFile("", spam_map);
			writeGramToFile("", ham_map);
		}
	} 
	
	/** 
	 * 
	 * @param p
	 * @param post_list
	 * @param type
	 */ 
	
	public void getNGramCountMapSelector(Post p,List<Post> post_list,String type) {

		String ngram_label = "";

		if (type.equalsIgnoreCase("giveaway")) { 

			ngram_label = p.getGiveawayLabel();

			if (ngram_label != null && ngram_label.equalsIgnoreCase("real")) { 

				getNGramCount(p, post_list, real_giveaway_count_map, type);
			}

			if (ngram_label != null && ngram_label.equalsIgnoreCase("fake")) { 

				getNGramCount(p, post_list, fake_giveaway_count_map, type);
			}
		}

		if (type.equalsIgnoreCase("sentiment")) { 

			ngram_label = p.getSentimentLabel();

			if (ngram_label != null && ngram_label.equalsIgnoreCase("positive")) { 

				getNGramCount(p, post_list, positive_count_map, type);
			}

			if (ngram_label != null && ngram_label.equalsIgnoreCase("negative")) {

				getNGramCount(p, post_list, negative_count_map, type);
			}

			if (ngram_label != null && ngram_label.equalsIgnoreCase("neutral")) { 

				getNGramCount(p, post_list, neutral_count_map, type);
			}
		}

		if (type.equalsIgnoreCase("spam")) { 

			ngram_label = p.getSpamLabel();

			if (ngram_label != null && ngram_label.equalsIgnoreCase("spam")) { 

				getNGramCount(p, post_list, spam_map, type);
			}

			if (ngram_label != null && ngram_label.equalsIgnoreCase("ham")) { 

				getNGramCount(p, post_list, ham_map, type);
			}
		}
	}

	/** 
	 * Returns the count of NGrams for a given NGram and a classification type
	 * @param p Post containing the post object along with the content and other things
	 * @param post_list List<Post> containing the posts for a given training set
	 * @param type Type of classification which needs to be done these include (Giveaway,Sentiment Analyis etc)
	 * @return Integer containing the count of integers
	 */ 

	public void getNGramCount(Post p,List<Post> post_list,Map<String,Double> ngram_map,String type) { 

		double count = 0;
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
			if (index != -1 && !ngram_map.containsKey(ngram)) {
				count = getNGramCountUtil(ngram, post_list, ngram_label, "giveaway");
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

	public double getNGramCountUtil(String ngram,List<Post> post_list, String ngram_label,String type) { 

		double count = 0.0;
		String sentence = ""; 
		String post_label = ""; 
		String spam_label = ""; 
		double label_count = 0.0; 

		if (ngram_label != null && (type.equalsIgnoreCase("giveaway") || type.equalsIgnoreCase("sentiment"))) { 

			label_count = label_count_map.get(ngram_label);
		} 

		if (ngram_label != null && type.equalsIgnoreCase("spam")) {  

			label_count = spam_count_map.get(ngram_label);
		} 

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

		return ((count+1)/(label_count+1));
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
	 * Given a map of NGram probabilities writes them to a file
	 * @param filename String containing the filename
	 * @param ngram_map_count Map<String,Double> containing ngram probabilities
	 */ 

	public void writeGramToFile(String filename,Map<String,Double> ngram_map_count) { 

		BufferedWriter bw;
		FileWriter fw;
		File f;
		double count;
		String transform = ""; 

		f = new File(filename); 

		if (!f.exists()) { 

			try {
				f.createNewFile();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}

		try { 

			fw = new FileWriter(filename,true);
			bw = new BufferedWriter(fw); 

			for (String s : ngram_map_count.keySet()) { 

				count = ngram_map_count.get(s);
				transform = getTransformedString(s);
				transform = transform + "=" + String.valueOf(count);
				bw.write(transform);
				bw.newLine();
			}

			bw.close();
			fw.close();

		} catch(Exception e) { 
			e.printStackTrace();
		}
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
	 * Main function to test the overall functionality of the class 
	 * @param args
	 */ 

	public static void main(String args[]) {  

		for (int i = 1; i <= 3; i++) { 
			
			new NGram().NGramExtractionPipeline("giveaway/split_1.txt",i,"Instagram","giveaway");
		}
	}
}
