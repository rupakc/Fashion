package org.kutty.giveaway;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kutty.analytics.InstagramAnalytics;
import org.kutty.analytics.LanguageAnalytics;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Performs Analytics on the Instagram Giveaways 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 29 August, 2015
 * 
 */

public class GiveawayAnalytics {
	
	/** 
	 * For a given time interval returns the mapping between the language and giveaway count 
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Map<String,Integer> containing the mapping between the language and the integer
	 */ 
	
	public static Map <String, Integer> getLanguageDistribution(Date from,Date to) { 

		Map <String, Integer> language_distribution;
		language_distribution = LanguageAnalytics.getLanguageProductChannel("giveaway","Instagram", from, to); 

		return language_distribution;
	}
	
	/** 
	 * For a given time interval returns the mapping between the country and its count
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Map<String,Integer> containing the map of Country and its giveaway count
	 */ 
	
	public static Map<String,Integer> getCountryDistribution(Date from,Date to) { 

		Map <String, Integer> country_distribution;
		country_distribution = InstagramAnalytics.getCountryMap("Giveaway", from, to);

		return country_distribution;
	}
	
	/** 
	 * Returns the number of giveaways during a given time interval
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Integer containing the number of fake giveaways
	 */ 
	
	public static int getFakeCount(Date from,Date to) { 

		int count = 0;
		MongoBase mongo = null;
		DBObject query; 
		double from_date;
		double to_date; 
		DBCollection collection; 

		try {  

			mongo = new MongoBase();
			mongo.setDB("Analytics");
			mongo.setCollection("Giveaway");
			from_date = DateConverter.getJulianDate(from);
			to_date = DateConverter.getJulianDate(to);
			collection = mongo.getCollection(); 

			query = new BasicDBObject("Channel","Instagram").append("ClassLabel", "fake").
					append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date)); 

			count = collection.find(query).size(); 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return count;
	}
	
	/** 
	 * Returns a set of top caption texts during a given interval for a given label (.i.e real/fake)
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param label String containing the label (i.e. real/fake)
	 * @return Set<String> containing the set of caption texts
	 */ 
	
	public static Set<String> getTopGiveawayLabels(Date from,Date to,String label) { 

		MongoBase mongo = null;
		DBObject query; 
		double from_date;
		double to_date; 
		DBCollection collection; 
		DBCursor cursor;
		DBObject temp;
		Set<String> caption_set = new HashSet<String>();
		label = label.toLowerCase().trim(); 
		
		try {  

			mongo = new MongoBase();
			mongo.setDB("Analytics");
			mongo.setCollection("Giveaway");
			from_date = DateConverter.getJulianDate(from);
			to_date = DateConverter.getJulianDate(to);
			collection = mongo.getCollection(); 

			query = new BasicDBObject("Channel","Instagram").append("ClassLabel", label).
					append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date)); 

			cursor = collection.find(query); 
			
			while(cursor.hasNext()) { 
				
				temp = cursor.next();
				caption_set.add((String) temp.get("CaptionText"));
			}

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return caption_set;
	}
	
	/** 
	 * Returns a map of users and their caption text
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param label String containing the label (i.e. real/fake)
	 * @return Map <String, String> containing the username and the caption text
	 */ 
	
	public static Map <String, String> getUsernameMap(Date from,Date to,String label) { 

		MongoBase mongo = null;
		DBObject query; 
		double from_date;
		double to_date; 
		DBCollection collection; 
		DBCursor cursor;
		DBObject temp;
		Map <String, String> caption_map = new HashMap <String, String>();
		label = label.toLowerCase().trim(); 
		
		try {  

			mongo = new MongoBase();
			mongo.setDB("Analytics");
			mongo.setCollection("Giveaway");
			from_date = DateConverter.getJulianDate(from);
			to_date = DateConverter.getJulianDate(to);
			collection = mongo.getCollection(); 

			query = new BasicDBObject("Channel","Instagram").append("ClassLabel", label).
					append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date)); 

			cursor = collection.find(query); 
			
			while(cursor.hasNext()) { 
				
				temp = cursor.next();
				caption_map.put((String) temp.get("UserName"), (String) temp.get("CaptionText"));
			}

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 

		return caption_map;
	}
	
	/** 
	 * Returns the number of giveaways during a given time interval
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Integer containing the number of fake giveaways
	 */ 
	
	public static int getRealCount(Date from,Date to) { 

		int count = 0;
		MongoBase mongo = null;
		DBObject query; 
		double from_date;
		double to_date; 
		DBCollection collection; 

		try {  

			mongo = new MongoBase();
			mongo.setDB("Analytics");
			mongo.setCollection("Giveaway");
			from_date = DateConverter.getJulianDate(from);
			to_date = DateConverter.getJulianDate(to);
			collection = mongo.getCollection(); 

			query = new BasicDBObject("Channel","Instagram").append("ClassLabel", "real").
					append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date)); 

			count = collection.find(query).size(); 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return count;
	}
}
