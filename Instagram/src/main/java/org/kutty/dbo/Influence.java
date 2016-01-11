package org.kutty.dbo;

import java.util.Date;

/**
 * Defines a Influence object for easy insertion in the database
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 8 September, 2015
 * 
 */ 

public class Influence {
	
	private String userId;
	private String userName;
	private String channel;
	private double followerFactor;
	private double commentFactor;
	private double likeFactor;
	private double followerWeight;
	private double commentWeight;
	private double likeWeight;
	private double index; 
	private Date lastUpdated;
	
	/**
	 * @return the userId
	 */ 
	
	public String getUserId() { 
		
		return userId;
	} 
	
	/**
	 * @param userId the userId to set
	 */ 
	
	public void setUserId(String userId) { 
		
		this.userId = userId;
	} 
	
	/** 
	 * 
	 * @return the userName
	 */ 
	
	public String getUserName() { 
		
		return userName;
	} 
	
	/** 
	 * 
	 * @param userName the userName to set
	 */ 
	
	public void setUserName(String userName) { 
		
		this.userName = userName;
	} 
	
	/** 
	 * 
	 * @return the channel
	 */ 
	
	public String getChannel() { 
		
		return channel;
	} 
	
	/** 
	 * 
	 * @param channel the channel to set
	 */ 
	
	public void setChannel(String channel) { 
		
		this.channel = channel;
	} 
	
	/** 
	 * 
	 * @return the followerFactor
	 */ 
	
	public double getFollowerFactor() { 
		
		return followerFactor;
	} 
	
	/** 
	 * 
	 * @param followerFactor the followerFactor to set
	 */ 
	
	public void setFollowerFactor(double followerFactor) { 
		
		this.followerFactor = followerFactor;
	} 
	
	/** 
	 * 
	 * @return the commentFactor
	 */ 
	
	public double getCommentFactor() { 
		
		return commentFactor;
	}
	
	/**
	 * 
	 * @param commentFactor the commentFactor to set
	 */ 
	
	public void setCommentFactor(double commentFactor) { 
		
		this.commentFactor = commentFactor;
	} 
	
	/** 
	 * 
	 * @return the likeFactor
	 */ 
	
	public double getLikeFactor() { 
		
		return likeFactor;
	} 
	
	/** 
	 * 
	 * @param likeFactor the likeFactor to set
	 */ 
	
	public void setLikeFactor(double likeFactor) { 
		
		this.likeFactor = likeFactor;
	} 
	
	/** 
	 * 
	 * @return the followerWeight
	 */ 
	
	public double getFollowerWeight() { 
		
		return followerWeight;
	} 
	
	/** 
	 * 
	 * @param followerWeight the followerWeight to set
	 */ 
	
	public void setFollowerWeight(double followerWeight) { 
		
		this.followerWeight = followerWeight;
	} 
	
	/** 
	 * 
	 * @return the commentWeight
	 */ 
	
	public double getCommentWeight() { 
		
		return commentWeight;
	} 
	
	/** 
	 * 
	 * @param commentWeight the commentWeight to set
	 */ 
	
	public void setCommentWeight(double commentWeight) { 
		
		this.commentWeight = commentWeight;
	} 
	
	/** 
	 * 
	 * @return the likeWeight
	 */ 
	
	public double getLikeWeight() { 
		
		return likeWeight;
	} 
	
	/** 
	 * 
	 * @param likeWeight the likeWeight to set
	 */ 
	
	public void setLikeWeight(double likeWeight) { 
		
		this.likeWeight = likeWeight;
	} 
	
	/** 
	 * 
	 * @return the index
	 */ 
	
	public double getIndex() { 
		
		return index;
	} 
	
	/**
	 * 
	 * @param index the index to set
	 */ 
	
	public void setIndex(double index) { 
		
		this.index = index;
	}

	/**
	 * @return the lastUpdated
	 */
	public Date getLastUpdated() { 
		
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Date lastUpdated) { 
		
		this.lastUpdated = lastUpdated;
	}
}
