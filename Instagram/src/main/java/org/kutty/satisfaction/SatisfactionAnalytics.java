package org.kutty.satisfaction;

import java.util.Date;

import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Performs satisfaction analytics on the satisfaction collection 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 23 September,2015
 */

public class SatisfactionAnalytics {
	
	/** 
	 * Calculates the satisfaction score on a given channel for a given product
	 * @param channel String containing the channel name
	 * @param brand String containing the brand name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return
	 */
	
	public static double getSatisfactionScore(String channel,String brand,Date from,Date to) { 
		
		MongoBase mongo;
		DBObject query;
		DBCollection collection;
		DBCursor cursor;
		DBObject temp;
		double score = 0.0;
		int size = 0; 
		
		try { 
			
			mongo = new MongoBase();
			mongo.setDB(Constants.ANALYTICS_DB);
			mongo.setCollection(Constants.SATISFACTION_COLLECTION); 
			
			if (!channel.equalsIgnoreCase("Instagram")) {  
				
				query = new BasicDBObject("Channel",channel).append("Product",brand).
						append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to)); 
				
			} else { 
				
				double fromDate = DateConverter.getJulianDate(from);
				double toDate = DateConverter.getJulianDate(to); 
				
				query = new BasicDBObject("Channel",channel).append("Product", brand).
						append("OtherDate", new BasicDBObject("$gte",fromDate).append("$lte",toDate));
			}
			
			collection = mongo.getCollection();
			cursor = collection.find(query); 
			size = cursor.size(); 
			
			while(cursor.hasNext()) { 
				
				temp = cursor.next();
				score = score + (double)temp.get("SatisfactionScore");
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return (score/(size+1));
	}
}
