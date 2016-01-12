package org.kutty.toptrends;

import org.kutty.clean.Clean;
import org.kutty.dbo.Topic;
import org.kutty.features.FeatureUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/** 
 * Defines a set of utility functions for implementation of LDA on a set of documents
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 11 January,2016
 */

public class LDAUtil {
	
	/** 
	 * Given a string removes the stopwords
	 * @param s String from which stopwords have to be removed
	 * @return String without the stopwords
	 */
	public static String removeStopWords(String s) {
		
		s = s.toLowerCase();
		return FeatureUtil.removeStopWords(s);
	}
	
	/** 
	 * Removes punctuations, new lines and similar junk from a given String
	 * @param s String from which these have to be removed
	 * @return String without the occurrence of such junk
	 */
	public static String removePunctuation(String s) { 
		
		s = Clean.cleanHTML(s);
		s = Clean.removeNewLines(s);
		s = s.replace(".", " ");
		s = Clean.removePunctuationAndJunk(s);
		
		return s;
	}
	
	/**
	 * Generates Random Number between 1 to maximum number of topics
	 * @param maxTopicNum Integer containing the maximum number of topics
	 * @return a random Integer between 1 and the max topic number
	 */
	public static int generateRandomNumber(int maxTopicNum) { 
		
		Random random = new Random();

		return random.nextInt(maxTopicNum) + 1;
	}
	
	/** 
	 * Generates the InitialDistribution of probabilities of word distribution indexes
	 * @param numberOfWords Integer containing the maximum number of words
	 * @param maxTopicNum Integer containing the maximum topic number
	 * @return Array[] containing the indices of the topic distribution
	 */
	public static int[] generateInitialDistribution(int numberOfWords,int maxTopicNum) { 
		
		int topicIndex[] = new int[numberOfWords]; 
		Random random = new Random(); 
		
		for (int i = 0; i < numberOfWords; i++) { 
			
			topicIndex[i] = (random.nextInt(maxTopicNum));
		}
		
		return topicIndex;
	}
	
	/** 
	 * Given a string tokenizes it based on the presence of spaces
	 * @param s String which is to be tokenized
	 * @return String array containing the set of word tokens
	 */
	public static String[] wordTokenize(String s) { 
		
		String words[] = s.split(" "); 
		
		for (int i = 0;i < words.length; i++) {   
			
			words[i] = words[i].trim();
		}
		
		return words;
	}
	
	/** 
	 * Initializes the topic objects in the topic array
	 * @param topicArray Topic array containing the topic objects
	 */
	public static void initTopicObjects(Topic[] topicArray) { 
		
		for (int i = 0; i < topicArray.length; i++) {  
			
			topicArray[i] = new Topic();
			topicArray[i].topicWords = new ArrayList<String>();
			topicArray[i].word_probabilities = new HashMap<String, Double>();
		}
	}
	
	/** 
	 * Returns the count of a given word in a given topic
	 * @param topic Topic on which the word is to be counted
	 * @param s String containing the word which is to be counted
	 * @return Double containing the word count
	 */
	public static double getWordCount(Topic topic,String s) { 
		
		double count = 0.0001;
		for (String word:topic.topicWords) { 
			if (word.equalsIgnoreCase(s)) { 
				count++;
			}
		}
		
		return count;
	}
}
