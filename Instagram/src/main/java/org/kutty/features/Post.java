package org.kutty.features;

/** 
 * Defines a Post entity for further processing
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 18 August, 2015 
 * 
 */ 

public class Post {
	
	private String content;
	private String tagset;
	private String sentimentLabel;
	private String spamLabel;
	private String giveawayLabel;
	
	/** 
	 * Returns the content of a given a post 
	 * @return String containing the content
	 */ 
	
	public String getContent() { 
		
		return content;
	}
	
	/** 
	 * Sets the content of a given String
	 * @param content String containing the content
	 */  
	
	public void setContent(String content) { 
		
		this.content = content;
	}
	
	/** 
	 * Sets the tagset of a given post
	 * @return String containing the tagset
	 */ 
	
	public String getTagset() { 
		
		return tagset;
	}
	
	/** 
	 * Sets the tagset of a given post (incase of Instagram only)
	 * @param tagset String containing the tagset
	 */ 
	
	public void setTagset(String tagset) { 
		
		this.tagset = tagset;
	}
	
	/** 
	 * Returns the sentiment label of a given post
	 * @return String containing the sentiment label of a post
	 */ 
	
	public String getSentimentLabel() { 
		
		return sentimentLabel;
	} 
	
	/** 
	 * Sets the sentiment label of a given post
	 * @param sentimentLabel String containing the sentiment label
	 */ 
	
	public void setSentimentLabel(String sentimentLabel) { 
		
		this.sentimentLabel = sentimentLabel;
	} 
	
	/** 
	 * Returns the spam label of a given post
	 * @return String containing the spam label
	 */ 
	
	public String getSpamLabel() { 
		
		return spamLabel;
	} 
	
	/** 
	 * Sets the spam label of a given post
	 * @param spamLabel String containing the spam label
	 */ 
	
	public void setSpamLabel(String spamLabel) { 
		
		this.spamLabel = spamLabel;
	} 
	
	/** 
	 * Returns the Giveaway label of a given post
	 * @return String containing the Giveaway label
	 */ 
	
	public String getGiveawayLabel() { 
		
		return giveawayLabel;
	} 
	
	/** 
	 * Sets the Giveaway label of a given post
	 * @param giveawayLabel String containing the giveaway label
	 */ 
	
	public void setGiveawayLabel(String giveawayLabel) { 
		
		this.giveawayLabel = giveawayLabel;
	}	
}
