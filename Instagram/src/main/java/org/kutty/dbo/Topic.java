package org.kutty.dbo;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;

import org.kutty.utils.SortUtil;

public class Topic {
	
	int topicNum;
	Map<String,Double> word_probabilities;
	String topicName; 
	
	
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
		SortedSet<Entry<String, Double>> sortEntries = SortUtil.entriesSortedByValues(this.word_probabilities);
		this.topicName = sortEntries.first().getKey();
	}
	
}
