package org.kutty.migrate;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.dbo.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Class for migration of data from one collection to another
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 7 November,2015
 */
public class MigrationService {
	
	/** 
	 * Returns a set of user objects present in the Central Fashion collection
	 * @return List<DBObject> containing the user objects
	 * @throws UnknownHostException
	 */
	public static List<DBObject> getInstagramUser() throws UnknownHostException { 
		
		List<DBObject> userList = new ArrayList<DBObject>();
		DBCollection collection;
		DBCursor cursor;
		BasicDBObject query;
		DBObject tempUser;
		MongoBase mongo; 
		
		query = new BasicDBObject("Channel","Instagram").append("Type", "user");
		mongo = new MongoBase();
		mongo.setCollection(Constants.FASHION_COLLECTION);
		collection = mongo.getCollection();
		cursor = collection.find(query);
		
		while(cursor.hasNext()) { 
			
			tempUser = cursor.next();
			userList.add(tempUser);
		}
		
		return userList;
	}
	
	/** 
	 * Converts the generic user objects into Instagram User objects
	 * @param userList List<DBObject> containing the user information retrieved from DB
	 * @return List<User> containing the Instagram user objects
	 */
	public static List<User> convertToInstagramUser(List<DBObject> userList) { 
		
		List<User> instagramUserList = new ArrayList<User>();
		User user;
		
		for (DBObject tempUser : userList) { 
			
			user = new User();
			user.setId((String) tempUser.get("UserId"));
			user.setUsername((String) tempUser.get("Username"));
			user.setBio((String) tempUser.get("Bio"));
			user.setWebsite((String) tempUser.get("Website"));
			user.setProfilePicture((String) tempUser.get("ProfilePicture"));
			user.setFullName((String) tempUser.get("Fullname"));
			user.setMediaCount((long) tempUser.get("MediaCount"));
			user.setFollowedByCount((long) tempUser.get("FollowedByCount"));
			user.setFollowsCount((long) tempUser.get("FollowsCount"));
			
			instagramUserList.add(user);
		}
		
		return instagramUserList;
	}
	
	/** 
	 * Inserts the Instagram User list in the user DB
	 * @param userList List<User> containing the Instagram user information
	 */
	public static void putInDB(List<User> userList) { 
		
		try {
			
			MongoBase mongo = new MongoBase();
			mongo.setCollection(Constants.USER_COLLECTION);
			for (User instaUser : userList) { 
				mongo.putInDB(instaUser);
			}
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	/** 
	 * Defines the pipeline for Instagram user migration
	 */
	public static void migrateInstagramUserPipeline() { 
		
		try {
			List<DBObject> userList = getInstagramUser();
			List<User> instaUser = convertToInstagramUser(userList);
			putInDB(instaUser);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) { 
		migrateInstagramUserPipeline();
	}
}
