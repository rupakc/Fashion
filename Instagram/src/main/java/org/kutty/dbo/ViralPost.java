package org.kutty.dbo;

/** 
 * Defines a viral post object containing the post related features for Virality Prediction
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 11 November,2015
 */
public class ViralPost {
	
	private String content;
	private Long spreadCount;
	private int hourOfDay;
	private int hashTags;
	private int punctCount;
	private int nounCount;
	private int verbCount;
	private int determinantCount;
	private int adjectiveCount;
	private int positiveWordCount;
	private int negativeWordCount; 
	
	/**
	 * @return the content
	 */
	public String getContent() { 
		
		return content;
	}
	
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) { 
		
		this.content = content;
	}
	
	/**
	 * @return the spreadCount
	 */
	public Long getSpreadCount() { 
		
		return spreadCount;
	} 
	
	/**
	 * @param spreadCount the spreadCount to set
	 */
	public void setSpreadCount(Long spreadCount) { 
		
		this.spreadCount = spreadCount;
	} 
	
	/**
	 * @return the hourOfDay
	 */
	public int getHourOfDay() { 
		
		return hourOfDay;
	} 
	
	/**
	 * @param hourOfDay the hourOfDay to set
	 */
	public void setHourOfDay(int hourOfDay) { 
		
		this.hourOfDay = hourOfDay;
	} 
	
	/**
	 * @return the hashTags
	 */
	public int getHashTags() { 
		
		return hashTags;
	} 
	
	/**
	 * @param hashTags the hashTags to set
	 */
	public void setHashTags(int hashTags) { 
		
		this.hashTags = hashTags;
	} 
	
	/**
	 * @return the punctCount
	 */
	public int getPunctCount() { 
		
		return punctCount;
	} 
	
	/**
	 * @param punctCount the punctCount to set
	 */
	public void setPunctCount(int punctCount) { 
		
		this.punctCount = punctCount;
	} 
	
	/**
	 * @return the nounCount
	 */
	public int getNounCount() { 
		
		return nounCount;
	} 
	
	/**
	 * @param nounCount the nounCount to set
	 */
	public void setNounCount(int nounCount) { 
		
		this.nounCount = nounCount;
	} 
	
	/**
	 * @return the verbCount
	 */
	public int getVerbCount() { 
		
		return verbCount;
	} 
	
	/**
	 * @param verbCount the verbCount to set
	 */
	public void setVerbCount(int verbCount) { 
		
		this.verbCount = verbCount;
	} 
	
	/**
	 * @return the determinantCount
	 */
	public int getDeterminantCount() { 
		
		return determinantCount;
	} 
	
	/**
	 * @param determinantCount the determinantCount to set
	 */
	public void setDeterminantCount(int determinantCount) { 
		
		this.determinantCount = determinantCount;
	} 
	
	/**
	 * @return the adjectiveCount
	 */
	public int getAdjectiveCount() { 
		
		return adjectiveCount;
	} 
	
	/**
	 * @param adjectiveCount the adjectiveCount to set
	 */
	public void setAdjectiveCount(int adjectiveCount) { 
		
		this.adjectiveCount = adjectiveCount;
	} 
	
	/**
	 * @return the positiveWordCount
	 */
	public int getPositiveWordCount() { 
		
		return positiveWordCount;
	} 
	
	/**
	 * @param positiveWordCount the positiveWordCount to set
	 */
	public void setPositiveWordCount(int positiveWordCount) { 
		
		this.positiveWordCount = positiveWordCount;
	} 
	
	/**
	 * @return the negativeWordCount
	 */
	public int getNegativeWordCount() { 
		
		return negativeWordCount;
	} 
	
	/**
	 * @param negativeWordCount the negativeWordCount to set
	 */
	public void setNegativeWordCount(int negativeWordCount) { 
		
		this.negativeWordCount = negativeWordCount;
	}
}	
