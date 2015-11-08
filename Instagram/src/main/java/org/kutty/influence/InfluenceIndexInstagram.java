package org.kutty.influence;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.classification.EnsembleMachineSentiment;
import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.dbo.Influence;
import org.kutty.utils.StatUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Calculates the Influence Index for a given user currently supports Instagram
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 8 September, 2015
 * 
 */ 

public class InfluenceIndexInstagram {

	private double followerFactor;
	private double commentFactor;
	private double likeFactor;
	private double followerWeight;
	private double commentWeight;
	private double likeWeight;  

	/** 
	 * Returns the set of userIds for Instagram present in the database
	 * @param db_name String containing the database name
	 * @param collection_name String containing the collection name
	 * @return Set<String> containing the userIds
	 */ 

	public static Set<String> getUserIds(String db_name,String collection_name) { 

		Set<String> userIds = new HashSet<String>();
		MongoBase mongo;
		DBCollection collection;
		DBObject query;
		DBCursor cursor;
		DBObject fields; 

		try {

			mongo = new MongoBase();
			mongo.setDB(db_name);
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			query = new BasicDBObject("Channel", "Instagram"); 
			fields = new BasicDBObject("UserId",1);
			cursor = collection.find(query, fields); 

			while (cursor.hasNext()) { 

				userIds.add((String) cursor.next().get("UserId"));
			}

		} catch (UnknownHostException e) { 

			e.printStackTrace();
		}

		return userIds;
	}

	/** 
	 * Returns the follower factor for a given user (ratio of followers/following)
	 * @param userId String containing the userId
	 * @return Double containing the follower factor
	 */ 

	public static double getFollowerFactor(String userId) { 

		double follower_factor = 0;
		MongoBase mongo;
		DBCollection collection;
		DBObject query;
		DBCursor cursor; 
		DBObject temp;
		Long followedBy;
		Long follows; 

		try { 

			mongo = new MongoBase();
			mongo.setCollection(Constants.USER_COLLECTION);
			collection = mongo.getCollection(); 
			query = new BasicDBObject("Channel","Instagram").append("UserId", userId);
			cursor = collection.find(query);  

			while(cursor.hasNext()) { 

				temp = cursor.next();
				followedBy = (Long) temp.get("FollowedByCount");
				follows = (Long) temp.get("FollowsCount");
				follower_factor = (followedBy*1.0/(follows+1));
			} 

		} catch (UnknownHostException e) { 

			e.printStackTrace();
		}

		return follower_factor;
	}
	
	/** 
	 * Given a userId returns the follower weight
	 * @param userId String containing the userId
	 * @return Double containing the follower weight
	 */
	public static Double getFollowerWeight(String userId) { 

		double followerWeight = 0.01;
		double median;
		double max;
		double min;
		DBCollection collection;
		DBObject query;
		DBObject fields; 
		DBCursor cursor;
		BasicDBList queryList;
		List<Double> likeCommentList = new ArrayList<Double>();
		DBObject temp;
		MongoBase mongo; 

		queryList = new BasicDBList();
		queryList.add(new BasicDBObject("Type","tag"));
		queryList.add(new BasicDBObject("Type","image"));
		queryList.add(new BasicDBObject("Type","video"));

		fields = new BasicDBObject("CommentCount", 1).append("LikeCount", 1);
		query = new BasicDBObject("Channel","Instagram").append("UserId", userId).append("$or", queryList);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(Constants.FASHION_COLLECTION);
			collection = mongo.getCollection();
			cursor = collection.find(query, fields); 

			while(cursor.hasNext()) { 

				temp = cursor.next();
				likeCommentList.add((double) ((Integer)temp.get("CommentCount") + (Integer)temp.get("LikeCount")));
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (likeCommentList.isEmpty() || likeCommentList.size() <= Constants.LIMIT_OF_SIG) {  

			return followerWeight;
		}

		median = StatUtils.getMedian(likeCommentList);
		max = Collections.max(likeCommentList);
		min = Collections.min(likeCommentList); 
		followerWeight = (median/((max-min) + 1));

		if (followerWeight == median) {  

			followerWeight = 0.01;
		}

		return followerWeight;
	}
	
	/** 
	 * Returns the comment factor for a given user (ratio of comments/posts)
	 * @param userId String containing the userId
	 * @return Double containing the comment factor
	 */ 

	public static double getCommentFactor(String userId) { 

		MongoBase mongo;
		DBCollection collection;
		DBObject query;
		DBObject temp;
		DBObject fields;
		DBCursor cursor;
		BasicDBList queryList;
		int post_size = 0;
		double total_comment_count = 0.0;

		try {

			mongo = new MongoBase();
			mongo.setCollection(Constants.FASHION_COLLECTION);
			collection = mongo.getCollection(); 
			queryList = new BasicDBList();
			queryList.add(new BasicDBObject("Type","tag"));
			queryList.add(new BasicDBObject("Type","image"));
			queryList.add(new BasicDBObject("Type","video"));
			query = new BasicDBObject("$or",queryList).append("Channel","Instagram").append("UserId", userId);
			fields = new BasicDBObject("CommentCount",1);
			cursor = collection.find(query,fields);
			post_size = cursor.size(); 

			while(cursor.hasNext()) { 

				temp = cursor.next();
				total_comment_count = total_comment_count + (Integer)temp.get("CommentCount"); 
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return ((total_comment_count*1.0)/(post_size+1));
	}
	
	/** 
	 * Given a userId returns the corresponding user name
	 * @param userId String containing the userId
	 * @return String containing the user name
	 */
	public static String getUserName(String userId) { 

		String userName = "";
		MongoBase mongo;
		DBCollection collection;
		DBObject query;
		DBObject fields;
		DBObject result;
		BasicDBList queryList; 

		queryList = new BasicDBList();
		queryList.add(new BasicDBObject("Type","tag"));
		queryList.add(new BasicDBObject("Type","image"));
		queryList.add(new BasicDBObject("Type","video"));

		query = new BasicDBObject("Channel","Instagram").append("UserId", userId).append("$or", queryList);
		fields = new BasicDBObject("Username",1);

		try {

			mongo = new MongoBase();
			mongo.setCollection(Constants.FASHION_COLLECTION);
			collection = mongo.getCollection(); 
			result = collection.findOne(query,fields);

			if(result != null && result.containsField("Username")) { 

				userName = (String) result.get("Username");
			}

		} catch(Exception e) { 

			e.printStackTrace();
		}

		return userName;
	}
	
	/** 
	 * Calculates the comment weight for a given userId
	 * @param userId String containing the user Id
	 * @return Double containing the value of the comment weight
	 */
	public static Double getCommentWeight(String userId) { 

		double commentWeight = 0.01;
		int positiveCount = 0;
		int negativeCount = 0;
		int totalCount = 1;
		String classLabel;
		DBObject query;
		DBObject fields;
		DBCursor cursor;
		DBCollection collection;
		MongoBase mongo; 
		EnsembleMachineSentiment ensembleSenti = new EnsembleMachineSentiment();

		String userName = getUserName(userId);

		if (!userName.isEmpty()) { 

			query = new BasicDBObject("Channel","Instagram").append("Type", "comment").
					append("UsernameTag", userName); 

			fields = new BasicDBObject("Message",1);

			try { 

				mongo = new MongoBase();
				mongo.setCollection(Constants.FASHION_COLLECTION);
				collection = mongo.getCollection();
				cursor = collection.find(query,fields);
				totalCount = cursor.size();

				if (cursor.size() <= 5) {  

					return commentWeight;
				}

				while(cursor.hasNext()) { 
					//TODO - Set channel to Instagram once data is ready to train model
					classLabel = ensembleSenti.organizeAndActEnsemble((String) cursor.next().get("Message"), "Twitter", 5); 

					if (classLabel.equalsIgnoreCase(Constants.POSITIVE_LABEL)) {  

						positiveCount++;
					}

					if (classLabel.equalsIgnoreCase(Constants.NEGATIVE_LABEL)) {  

						negativeCount++;
					}
				}

				commentWeight = (positiveCount - negativeCount)/totalCount;

			} catch (UnknownHostException e) { 

				e.printStackTrace();
			}
		}

		return commentWeight;
	}

	/** 
	 * Returns the like factor for a given user (ratio of likes/posts)
	 * @param userId String containing the userId
	 * @return Double containing the like factor
	 */ 

	public static double getLikeFactor(String userId) { 

		MongoBase mongo;
		DBCollection collection;
		DBObject query;
		DBObject temp;
		DBObject fields;
		DBCursor cursor;
		BasicDBList queryList;
		int post_size = 0;
		double total_like_count = 0.0;

		try {

			mongo = new MongoBase();
			mongo.setCollection(Constants.FASHION_COLLECTION);
			collection = mongo.getCollection(); 
			queryList = new BasicDBList();
			queryList.add(new BasicDBObject("Type","tag"));
			queryList.add(new BasicDBObject("Type","image"));
			queryList.add(new BasicDBObject("Type","video"));
			query = new BasicDBObject("$or",queryList).append("Channel","Instagram").append("UserId", userId);
			fields = new BasicDBObject("LikeCount",1);
			cursor = collection.find(query,fields);
			post_size = cursor.size(); 

			while(cursor.hasNext()) { 

				temp = cursor.next();
				total_like_count = total_like_count + (Integer)temp.get("LikeCount"); 
			}

		} catch (UnknownHostException e) { 

			e.printStackTrace();
		}

		return ((total_like_count*1.0)/(post_size+1));
	}
	
	/** 
	 * Given a userId calculates the like weight
	 * @param userId String containing the userId
	 * @return Double containing the like weight
	 */
	public static Double getLikeWeight(String userId) { 

		double likeWeight = 0.01;
		DBObject query;
		DBObject fields;
		DBObject temp;
		DBCollection collection; 
		DBCursor cursor;
		MongoBase mongo;
		List<Double> likeFactorList = new ArrayList<Double>();
		DateTime to = new DateTime();
		Double median = 0.0;
		Double greaterThanCount = 0.0;
		Double lessThanCount = 0.0;
		DateTime from = to.minusDays(Constants.MEDIAN_DAYS);


		query = new BasicDBObject("Channel","Instagram").append("UserId", userId).
				append("LastUpdated", new BasicDBObject("$lte",to.toDate()).append("$gte", from.toDate())); 

		fields = new BasicDBObject("LikeFactor",1);

		try { 

			mongo = new MongoBase();
			mongo.setDB(Constants.ANALYTICS_DB);
			mongo.setCollection(Constants.INFLUENCE_COLLECTION);
			collection = mongo.getCollection();
			cursor = collection.find(query, fields);

			if (!cursor.hasNext()) { 

				return likeWeight;
			}

			while(cursor.hasNext()) { 

				temp = cursor.next();
				likeFactorList.add((Double) temp.get("LikeFactor"));
			}

			median = StatUtils.getMedian(likeFactorList); 

			for(Double factor : likeFactorList) { 

				if (factor > median) {  

					greaterThanCount++; 

				} else if (factor < median) { 

					lessThanCount++;
				}
			}

			likeWeight = (greaterThanCount/(lessThanCount + 1));

		} catch (UnknownHostException e) { 

			e.printStackTrace();
		}

		if (likeWeight == 0.0) {  

			likeWeight = 0.01;
		}

		return likeWeight;
	}

	/** 
	 * Given a userId returns the influence index of the user
	 * @param userId String containing the userId
	 * @return Double containing the influence index
	 */

	public double getInfluenceIndex(String userId) { 

		double influenceIndex = 0.0;

		followerFactor = getFollowerFactor(userId);
		commentFactor = getCommentFactor(userId);
		likeFactor = getLikeFactor(userId);
		followerWeight = getFollowerWeight(userId);
		commentWeight = getCommentWeight(userId); 
		likeWeight = getLikeWeight(userId);

		influenceIndex = followerWeight*followerFactor + commentWeight*commentFactor + likeWeight*likeFactor;

		return influenceIndex;
	}
	
	/** 
	 * Defines the pipeline for user influence calculation
	 * @param dbName String containing the database name
	 * @param collectionName String containing the collection name
	 * @throws UnknownHostException
	 */
	public void influencePipeline(String dbName,String collectionName) throws UnknownHostException { 

		Set<String> userIds = getUserIds(dbName,collectionName);
		Influence influence;
		String userName = ""; 
		MongoBase mongo = new MongoBase(); 
		mongo.setDB(Constants.ANALYTICS_DB);
		mongo.setCollection(Constants.INFLUENCE_COLLECTION); 
		
		for (String userId : userIds) {  

			userName = getUserName(userId); 
			
			if (!userName.isEmpty()) { 
				
				influence = new Influence();
				influence.setChannel("Instagram");
				influence.setUserId(userId);
				influence.setUserName(userName);
				influence.setLastUpdated(new Date());
				influence.setIndex(getInfluenceIndex(userId));
				influence.setCommentFactor(commentFactor);
				influence.setCommentWeight(commentWeight);
				influence.setFollowerFactor(followerFactor);
				influence.setFollowerWeight(followerWeight);
				influence.setLikeFactor(likeFactor);
				influence.setLikeWeight(likeWeight);
				
				mongo.putInDB(influence);
			}
		}
	}

	/** 
	 * Main function to test the functionality of the class
	 * @param args
	 */ 

	public static void main(String args[]) { 
		
		InfluenceIndexInstagram in = new InfluenceIndexInstagram();
		try {
			in.influencePipeline("Central", "User");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
