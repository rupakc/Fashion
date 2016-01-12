package org.kutty.dbo;

import java.util.Date;
import java.util.List;

/** 
 * Defines the object for detecting top trends for a given channel
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 12 January,2016
 */

public class ChannelTrend {
	
	public String channelName;
	public Date startDate;
	public Date endDate;
	public Topic[] topicList;
	public List<Object> postIds; 
	
	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	} 
	
	/**
	 * @param channelName the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	} 
	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	} 
	
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	} 
	
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	} 
	
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	} 
	
	/**
	 * @return the topicList
	 */
	public Topic[] getTopicList() {
		return topicList;
	} 
	
	/**
	 * @param topicList the topicList to set
	 */
	public void setTopicList(Topic[] topicList) {
		this.topicList = topicList;
	}
	
	/**
	 * @return the postIds
	 */
	public List<Object> getPostIds() {
		return postIds;
	} 
	
	/**
	 * @param postIds the postIds to set
	 */
	public void setPostIds(List<Object> postIds) {
		this.postIds = postIds;
	}
}
