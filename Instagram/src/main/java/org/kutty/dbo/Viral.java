package org.kutty.dbo;

import java.util.Date;

/** 
 * Defines a viral object which is to be inserted in the database
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 15 November, 2015
 */
public class Viral {
	
	private String channel;
	private String content;
	private Date timeStamp;
	private double probability;
	private double otherDate;
	private String label;
	
	/**
	 * @return the channel
	 */
	public String getChannel() { 
		
		return channel;
	}
	
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) { 
		
		this.channel = channel;
	} 
	
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
	 * @return the timeStamp
	 */
	public Date getTimeStamp() { 
		
		return timeStamp;
	} 
	
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) { 
		
		this.timeStamp = timeStamp;
	} 
	
	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	} 
	
	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) { 
		
		this.probability = probability;
	} 
	
	/**
	 * @return the otherDate
	 */
	public double getOtherDate() { 
		
		return otherDate;
	} 
	
	/**
	 * @param otherDate the otherDate to set
	 */
	public void setOtherDate(double otherDate) { 
		
		this.otherDate = otherDate;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) { 
		
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Viral [channel=" + channel + ", content=" + content
				+ ", timeStamp=" + timeStamp + ", probability=" + probability
				+ ", otherDate=" + otherDate + ", label=" + label + "]";
	}
}
