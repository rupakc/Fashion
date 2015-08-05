package org.kutty.analytics;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Performs basic analytics on the Instagram tags and associated content stored in the db
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 18 July, 2015
 */ 

public class InstagramAnalytics {

	public static String product_name;
	public static String collection_name = "Fashion";
	
	/** 
	 * 
	 * @param product_name
	 * @param from
	 * @param to
	 * @param n
	 * @return
	 */ 
	
	public static Set<String> getMostRecentTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null ; 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));
		
		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date));

		try { 
			
			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query); 
			cursor = cursor.sort(new BasicDBObject("Timestamp",-1)).limit(n); 
			
			while (cursor.hasNext()) { 
				
				temp = cursor.next();
				tag_set.add((String) temp.get("CaptionText"));
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}
		
		return tag_set;
	}
	
	/** 
	 * 
	 * @param product_name
	 * @param from
	 * @param to
	 * @param n
	 * @return
	 */ 
	
	public static Set<String> getMostLikedTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));
		
		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 
		
		fields = new BasicDBObject("CaptionText",1).append("LikeCount", 1).append("Timestamp", 1);

		try { 
			
			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 
		
			while (cursor.hasNext()) { 
				
				temp = cursor.next();
				System.out.println(temp.get("LikeCount"));
				tag_set.add((String) temp.get("CaptionText"));
			} 
			
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}
		
		return tag_set;
	}
	
	/** 
	 * 
	 * @param product_name
	 * @param from
	 * @param to
	 * @param n
	 * @return
	 */ 
	
	public static Set<String> getMostCommentedTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));
		
		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 
		
		fields = new BasicDBObject("CaptionText",1).append("CommentCount", 1).append("Timestamp", 1);

		try { 
			
			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("CommentCount",-1)).limit(n); 
		
			while (cursor.hasNext()) { 
				
				temp = cursor.next();
				System.out.println(temp.get("CommentCount"));
				tag_set.add((String) temp.get("CaptionText"));
			} 
			
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			mongo.closeConnection();
		}
		
		return tag_set;
	} 
	
	public static void main(String args[]) { 
		
		DateTime now = new DateTime();
		DateTime prev = now.minusYears(2);
		System.out.println(getMostCommentedTags("Zara",prev.toDate(),now.toDate(), 17));
	}
}
