package org.kutty.influence;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.kutty.db.MongoBase;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject; 

/** 
 * Calculates the Influence Index for a given Index currently supports Instagram
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 8 September, 2015
 * 
 */ 

public class InfluenceIndex {

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
			query = new BasicDBObject("Type","user").append("Channel", "Instagram"); 
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
				System.out.println(follower_factor);
			} 

		} catch (UnknownHostException e) { 

			e.printStackTrace();
		}

		return follower_factor;
	}

	/** 
	 * Main function to test the functionality of the class
	 * @param args
	 */ 

	public static void main(String args[]) { 

		Set<String> users = getUserIds("Central", "Fashion");
		for (String s : users) { 
			getFollowerFactor(s);
		}
	}
}
