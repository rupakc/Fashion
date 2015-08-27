package org.kutty.dbo;

/** 
 * Defines a giveaway object for easy insertion in the db
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 26 August, 2015
 * 
 */ 

public class Giveaway {
	
	private String captionText;
	private String tagSet;
	private double timeStamp;
	private String classLabel;
	private String channel; 
	private String userName; 
	
	public Giveaway() { 
		
		captionText = "";
		tagSet = "";
		timeStamp = 0.0;
		classLabel = "";
		userName = "";
		channel = "Instagram";
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getCaptionText() { 
		
		return captionText;
	} 
	
	/** 
	 * 
	 * @param captionText
	 */ 
	
	public void setCaptionText(String captionText) { 
		
		this.captionText = captionText;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getTagSet() { 
		
		return tagSet;
	} 
	
	/** 
	 * 
	 * @param tagSet
	 */
	 
	public void setTagSet(String tagSet) { 
		
		this.tagSet = tagSet;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public double getTimeStamp() { 
		
		return timeStamp;
	} 
	
	/** 
	 * 
	 * @param timeStamp
	 */
	
	public void setTimeStamp(double timeStamp) { 
		
		this.timeStamp = timeStamp;
	} 
	
	/** 
	 * 
	 * @return
	 */ 
	
	public String getClassLabel() { 
		
		return classLabel;
	} 
	
	/** 
	 * 
	 * @param classLabel
	 */
	
	public void setClassLabel(String classLabel) { 
		
		this.classLabel = classLabel;
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
	
	public String getUserName() { 
		
		return userName;
	}
	
	/** 
	 * 
	 * @param userName
	 */ 
	
	public void setUserName(String userName) { 
		
		this.userName = userName;
	}
}
