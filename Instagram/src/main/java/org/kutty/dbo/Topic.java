package org.kutty.dbo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;

import org.kutty.utils.SortUtil;

public class Topic {

	public int topicNum;
	public Map<String,Double> word_probabilities;
	public String topicName;
	public List<String> topicWords;
	public double topicProbability;

	/**
	 * @return the topicProbability
	 */
	public double getTopicProbability() {
		return topicProbability;
	}

	/**
	 * @param topicProbability the topicProbability to set
	 */
	public void setTopicProbability(double topicProbability) {
		this.topicProbability = topicProbability;
	}

	/**
	 * @return the topicWords
	 */
	public List<String> getTopicWords() {
		return topicWords;
	}

	/**
	 * @param topicWords the topicWords to set
	 */
	public void setTopicWords(List<String> topicWords) {
		this.topicWords = topicWords;
	}

	/**
	 * @return the topicNum
	 */
	public int getTopicNum() {
		return topicNum;
	} 

	/**
	 * 
	 * @param topicNum the topicNum to set
	 */
	public void setTopicNum(int topicNum) {
		this.topicNum = topicNum;
	} 

	/** 
	 * @return the word_probabilities
	 */ 

	public Map<String, Double> getWord_probabilities() {
		return word_probabilities;
	} 

	/**
	 * @param word_probabilities the word_probabilities to set
	 */
	public void setWord_probabilities(Map<String, Double> word_probabilities) {
		this.word_probabilities = word_probabilities;
	}

	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * @param topicName the topicName to set
	 */
	public void setTopicName() { 
		
		String emptyString = ""; 
		
		if (this.word_probabilities.containsKey(emptyString)) {  
			
			this.word_probabilities.remove(emptyString);
		}
		
		SortedSet<Entry<String, Double>> sortEntries = SortUtil.entriesSortedByValues(this.word_probabilities); 
		
		this.topicName = sortEntries.first().getKey();
	}

}
