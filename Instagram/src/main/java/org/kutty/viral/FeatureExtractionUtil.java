package org.kutty.viral;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/** 
 * Utility class for extracting a set of features for post virality prediction
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 9 November,2015
 */
public class FeatureExtractionUtil {
	
	
	public static List<BasicDBObject> getPostForChannel(String dbName,String collectionName,String channel,Date from,Date to) { 
		
		List<BasicDBObject> postList = new ArrayList<BasicDBObject>();
		MongoBase mongo;
		BasicDBObject query;
		DBCollection collection;
		DBCursor cursor; 
		
		query = queryGenerator(channel, from, to); 
		
		try { 
			
			mongo = new MongoBase();
			mongo.setDB(dbName);
			mongo.setCollection(collectionName); 
			
			collection = mongo.getCollection();
			cursor = collection.find(query);
			
			while(cursor.hasNext()) { 
				
				if (!channel.equalsIgnoreCase("Instagram")) {  
					
					postList.add((BasicDBObject) cursor.next()); 
					
				} else { 
					
					postList.add((BasicDBObject) cursor.next());
				}
			} 
			
		} catch (UnknownHostException e) { 
			
			e.printStackTrace();
		} 
		
		return postList;
	}
	
	public static BasicDBObject queryGenerator(String channel,Date from,Date to) { 
		
		BasicDBObject query = null;
		BasicDBList queryList; 
		
		if (channel.equalsIgnoreCase("Reddit")) {  
			
			double fromDate = DateConverter.getJulianDate(from);
			double toDate = DateConverter.getJulianDate(to); 
			
			query = new BasicDBObject("Channel",channel).
					append("TimeStamp", new BasicDBObject("$gte",fromDate).append("$lte", toDate));
		}
		
		else if (channel.equalsIgnoreCase("Instagram")) { 
			
			double fromDate = DateConverter.getJulianDate(from);
			double toDate = DateConverter.getJulianDate(to); 
			queryList = new BasicDBList(); 
			
			queryList.add(new BasicDBObject("Type","image"));
			queryList.add(new BasicDBObject("Type","tag"));
			queryList.add(new BasicDBObject("Type","video"));
			
			query = new BasicDBObject("Channel",channel).append("$or",queryList).
					append("Timestamp", new BasicDBObject("$gte",fromDate).append("$lte",toDate));
		}
		
		else { 
			
			query = new BasicDBObject("Channel",channel).
					append("TimeStamp", new BasicDBObject("$gte",from).append("$lte", to));
		}
		
		return query;
	}
	
	public static void main(String args[]) throws UnknownHostException { 
		
		DateTime to = new DateTime();
		DateTime from = to.minusMonths(3); 
		//System.out.println(MongoDBUtils.getMaxMinValue("Central","Forever21", "CommentCount", "max"));
		System.out.println(getPostForChannel("Central", "Forever21", "Instagram", from.toDate(), to.toDate()));
	}
}
