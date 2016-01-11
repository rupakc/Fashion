package org.kutty.toptrends;

import org.kutty.clean.Clean;
import org.kutty.features.FeatureUtil;

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
			
			topicIndex[i] = (random.nextInt(maxTopicNum) + 1);
		}
		
		return topicIndex;
	}
}
