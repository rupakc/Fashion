package org.kutty.influence;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.parser.ParseException;
import org.kutty.db.MongoBase;
import org.kutty.fetch.UserFetch;

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
 * TODO - Add the like,comment and follower weights
 */ 

public class InfluenceIndexInstagram {

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
			mongo.setCollection("Fashion");
			collection = mongo.getCollection(); 
			query = new BasicDBObject("Type","user").append("UserId", userId);
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
		int post_size = 0;
		double total_comment_count = 0.0;
		
		try {
			
			mongo = new MongoBase();
			mongo.setCollection("Fashion");
			collection = mongo.getCollection(); 
			query = new BasicDBObject("Type","tag").append("Channel","Instagram").append("UserId", userId);
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
		int post_size = 0;
		double total_like_count = 0.0;
		
		try {
			
			mongo = new MongoBase();
			mongo.setCollection("Fashion");
			collection = mongo.getCollection(); 
			query = new BasicDBObject("Type","tag").append("Channel","Instagram").append("UserId", userId);
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
	 * Given a userId returns the influence index of the user
	 * @param userId String containing the userId
	 * @return Double containing the influence index
	 */
	
	public static double getInfluenceIndex(String userId) { 
		
		double influenceIndex = 0.0;
		double followerFactor;
		double commentFactor;
		double likeFactor;
		double followerWeight;
		double commentWeight;
		double likeWeight; 
		
		followerFactor = getFollowerFactor(userId);
		commentFactor = getCommentFactor(userId);
		likeFactor = getLikeFactor(userId);
		followerWeight = 1.0;
		commentWeight = 1.0; //TODO - Replace the weights here with the functions
		likeWeight = 1.0;
		
		influenceIndex = followerWeight*followerFactor + commentWeight*commentFactor + likeWeight*likeFactor;
		
		return influenceIndex;
	}
	
	/** 
	 * Main function to test the functionality of the class
	 * @param args
	 */ 

	public static void main(String args[]) { 

		Set<String> users = getUserIds("Central", "Fashion");
		UserFetch user = new UserFetch(); 
		
		for (String s : users) { 
			
			try {
				user.fetchAndStore(s.trim());
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
