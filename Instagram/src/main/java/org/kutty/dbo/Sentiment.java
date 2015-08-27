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
	
	/**
	 * @return
	 */
	public String getProduct() { 
		
		return product;
	} 
	
	public void setProduct(String product) { 
		
		this.product = product;
	}
	
	public HashMap<String, Double> getSentimentDistribution() { 
		
		return sentimentDistribution;
	}
	
	public void setSentimentDistribution(HashMap<String, Double> sentimentDistribution) { 
		
		this.sentimentDistribution = sentimentDistribution;
	}
	
	public String getAuthor() {
		
		return author;
	}
	
	public void setAuthor(String author) {
		
		this.author = author;
	}
	
	public Date getTimestamp() { 
		
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) { 
		
		this.timestamp = timestamp;
	}
	
	public String getChannel() { 
		
		return channel;
	}
	
	public void setChannel(String channel) { 
		
		this.channel = channel;
	}
	
	public String getSentimentLabel() { 
		
		return sentimentLabel;
	}
	
	public void setSentimentLabel(String sentimentLabel) {  
		
		this.sentimentLabel = sentimentLabel;
	}
	
	public String getContent() { 
		
		return content;
	}
	
	public void setContent(String content) { 
		
		this.content = content;
	}
	
	public Double getSentimentScore() { 
		
		return sentimentScore;
	}
	
	public void setSentimentScore(Double sentimentScore) { 
		
		this.sentimentScore = sentimentScore;
	}
	
	public Double getOtherDate() { 
		
		return otherDate;
	}
	
	public void setOtherDate(Double otherDate) { 
		
		this.otherDate = otherDate;
	}
}
