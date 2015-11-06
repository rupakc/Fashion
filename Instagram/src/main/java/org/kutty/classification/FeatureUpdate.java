package org.kutty.classification;

import java.util.Map;

import org.kutty.dbo.Update;
import org.kutty.features.FeatureUtil;

/** 
 * Defines the update module for off-line update of the feature base of the models
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 6 November, 2015
 */
public class FeatureUpdate {

	public Map <String, Double> positiveMap;
	public Map <String, Double> negativeMap;
	public Map <String, Double> neutralMap;
	public Map <String, Double> positiveTagMap;
	public Map <String, Double> negativeTagMap; 
	public Map <String, Double> neutralTagMap; 
	public Map <String, Double> spamMap;
	public Map <String, Double> hamMap;
	public Map <String, Double> spamTagMap;
	public Map <String, Double> hamTagMap;
	public Map <String, Double> realMap;
	public Map <String, Double> fakeMap;
	public Map <String, Double> realTagMap;
	public Map <String, Double> fakeTagMap; 

	public String POSITIVE_FILENAME = "/positive_";
	public String NEGATIVE_FILENAME = "/negative_";
	public String NEUTRAL_FILENAME = "/neutral_";
	public String POSITIVE_TAG_FILENAME = "/tag_positive_";
	public String NEGATIVE_TAG_FILENAME = "/tag_negative_";
	public String NEUTRAL_TAG_FILENAME = "/tag_neutral_";
	public String REAL_FILENAME = "giveaway/real_";
	public String FAKE_FILENAME = "giveaway/fake_";
	public String REAL_TAG_FILENAME = "giveaway/tag_real_";
	public String FAKE_TAG_FILENAME = "giveaway/tag_fake_";
	public String SPAM_FILENAME = "/spam_";
	public String HAM_FILENAME = "/ham_";
	public String SPAM_TAG_FILENAME = "/tag_spam_";
	public String HAM_TAG_FILENAME = "/tag_ham_";
	
	public String CHANNEL_NAME;
	public int MODEL_NUMBER;
	public int NGRAM_NUMBER;
	public String CLASS_LABEL;
	public String content; 
	
	public void initSentimentMap(String channelName,Update update) { 
		
		update.getModelNum();
		update.getNgramNum();
		update.getClassLabel();
		
		if (update.getClassLabel().equalsIgnoreCase("positive")) { 
			
			this.POSITIVE_FILENAME = channelName + this.POSITIVE_FILENAME + update.getModelNum() + ".txt";
			positiveMap = LoadModel.getTrainedModel(this.POSITIVE_FILENAME);
			Map<String,Integer> countMap = Prior.generateLabelSentimentCountMap(channelName, update.getModelNum());
			content = FeatureUtil.getNGram(content, update.getNgramNum());
			
		}
	}
	
}
