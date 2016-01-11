package org.kutty.dbo;

import java.util.Date;
import java.util.List;

/** 
 * Defines the database object for a Twitter user profile 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 3 October,2015
 */

public class TweetUser {
	
	private Date activeSince;
	private String description;
	private Long followerCount;
	private Long favoriteCount;
	private Long friendCount;
	private Long statusCount;
	private String Url;
	private Long userId;
	private String userLang;
	private String userScreenName;
	private List<String> followerList;
	private List<String> friendList; 
	
	/**
	 * @return the activeSince
	 */
	public Date getActiveSince() { 
		
		return activeSince;
	} 
	
	/**
	 * @param activeSince the activeSince to set
	 */
	public void setActiveSince(Date activeSince) { 
		
		this.activeSince = activeSince;
	} 
	
	/**
	 * @return the description
	 */
	public String getDescription() { 
		
		return description;
	} 
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) { 
		
		this.description = description;
	} 
	
	/**
	 * @return the followerCount
	 */
	public Long getFollowerCount() { 
		
		return followerCount;
	} 
	
	/**
	 * @param followerCount the followerCount to set
	 */
	public void setFollowerCount(Long followerCount) { 
		
		this.followerCount = followerCount;
	} 
	
	/**
	 * @return the favoriteCount
	 */
	public Long getFavoriteCount() { 
		
		return favoriteCount;
	} 
	
	/**
	 * @param favoriteCount the favoriteCount to set
	 */
	public void setFavoriteCount(Long favoriteCount) { 
		
		this.favoriteCount = favoriteCount;
	} 
	
	/**
	 * @return the friendCount
	 */
	public Long getFriendCount() { 
		
		return friendCount;
	} 
	
	/**
	 * @param friendCount the friendCount to set
	 */
	public void setFriendCount(Long friendCount) { 
		
		this.friendCount = friendCount;
	} 
	
	/**
	 * @return the statusCount
	 */
	public Long getStatusCount() { 
		
		return statusCount;
	} 
	
	/**
	 * @param statusCount the statusCount to set
	 */
	public void setStatusCount(Long statusCount) { 
		
		this.statusCount = statusCount;
	} 
	
	/**
	 * @return the url
	 */
	public String getUrl() { 
		
		return Url;
	} 
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) { 
		
		Url = url;
	} 
	
	/**
	 * @return the userId
	 */
	public Long getUserId() { 
		
		return userId;
	} 
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) { 
		
		this.userId = userId;
	} 
	
	/**
	 * @return the userLang
	 */
	public String getUserLang() { 
		
		return userLang;
	} 
	
	/**
	 * @param userLang the userLang to set
	 */
	public void setUserLang(String userLang) { 
		
		this.userLang = userLang;
	} 
	
	/**
	 * @return the userScreenName
	 */
	public String getUserScreenName() { 
		
		return userScreenName;
	} 
	
	/**
	 * @param userScreenName the userScreenName to set
	 */
	public void setUserScreenName(String userScreenName) { 
		
		this.userScreenName = userScreenName;
	} 
	
	/**
	 * @return the followerList
	 */
	public List<String> getFollowerList() { 
		
		return followerList;
	} 
	
	/**
	 * @param followerList the followerList to set
	 */
	public void setFollowerList(List<String> followerList) { 
		
		this.followerList = followerList;
	} 
	
	/**
	 * @return the friendList
	 */
	public List<String> getFriendList() { 
		
		return friendList;
	} 
	
	/**
	 * @param friendList the friendList to set
	 */
	public void setFriendList(List<String> friendList) { 
		
		this.friendList = friendList;
	}
}
