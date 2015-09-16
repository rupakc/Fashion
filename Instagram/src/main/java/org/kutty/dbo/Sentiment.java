package org.kutty.dbo;

import java.util.Date;
import java.util.HashMap;

public class Sentiment {
	
	private String product;
	private HashMap <String, Double> sentimentDistribution;
	private String author;
	private Date timestamp;
	private String channel;
	private String sentimentLabel;
	private String content;
	private Double sentimentScore;
	private Double otherDate; 
	private int modelNumber;
	private int ngramNumber; 
	
	/**
	 * @return
	 */
	public String getProduct() { 
		
		return product;
	} 
	
	/** 
	 * 
	 * @param product
	 */ 
	
	public void setProduct(String product) { 
		
		this.product = product;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public HashMap<String, Double> getSentimentDistribution() { 
		
		return sentimentDistribution;
	}
	
	/** 
	 * 
	 * @param sentimentDistribution
	 */ 
	
	public void setSentimentDistribution(HashMap<String, Double> sentimentDistribution) { 
		
		this.sentimentDistribution = sentimentDistribution;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getAuthor() {
		
		return author;
	}
	
	/** 
	 * 
	 * @param author
	 */ 
	
	public void setAuthor(String author) {
		
		this.author = author;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public Date getTimestamp() { 
		
		return timestamp;
	}
	
	/** 
	 * 
	 * @param timestamp
	 */ 
	
	public void setTimestamp(Date timestamp) { 
		
		this.timestamp = timestamp;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getChannel() { 
		
		return channel;
	}
	
	/** 
	 * 
	 * @param channel
	 */ 
	
	public void setChannel(String channel) { 
		
		this.channel = channel;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getSentimentLabel() { 
		
		return sentimentLabel;
	}
	
	/** 
	 * 
	 * @param sentimentLabel
	 */ 
	
	public void setSentimentLabel(String sentimentLabel) {  
		
		this.sentimentLabel = sentimentLabel;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getContent() { 
		
		return content;
	}
	
	/** 
	 * 
	 * @param content
	 */ 
	
	public void setContent(String content) { 
		
		this.content = content;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public Double getSentimentScore() { 
		
		return sentimentScore;
	}
	
	/** 
	 * 
	 * @param sentimentScore
	 */ 
	
	public void setSentimentScore(Double sentimentScore) { 
		
		this.sentimentScore = sentimentScore;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public Double getOtherDate() { 
		
		return otherDate;
	}
	
	/** 
	 * 
	 * @param otherDate
	 */
	
	public void setOtherDate(Double otherDate) { 
		
		this.otherDate = otherDate;
	}

	/**
	 * @return the modelNumber
	 */ 
	
	public int getModelNumber() { 
		
		return modelNumber;
	}

	/** 
	 * 
	 * @param modelNumber the modelNumber to set
	 */ 
	
	public void setModelNumber(int modelNumber) { 
		
		this.modelNumber = modelNumber;
	}

	/** 
	 * 
	 * @return the ngramNumber
	 */ 
	
	public int getNgramNumber() { 
		
		return ngramNumber;
	}

	/** 
	 * 
	 * @param ngramNumber the ngramNumber to set
	 */ 
	
	public void setNgramNumber(int ngramNumber) { 
		
		this.ngramNumber = ngramNumber;
	}
}
