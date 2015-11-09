package org.kutty.influence;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.utils.MongoDBUtils;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Carries out analytics on the influence index calculated 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 23 September,2015
 *
 */ 

public class InfluenceAnalytics {
	
	/** 
	 * Returns a list of top users with highest influence index
	 * @param n Integer containing the number of users to return
	 * @return List<String> containing the names of top N users
	 */
	public static List<String> getTopUsers(int n) { 

		DBObject query;
		DBObject fields;
		DBObject orderBy;
		DBCollection collection;
		DBCursor cursor;
		MongoBase mongo;
		List<String> topList = new ArrayList<String>();

		query = new BasicDBObject("Channel","Instagram");
		fields = new BasicDBObject("UserName",1).append("Index", 1);
		orderBy = new BasicDBObject("Index",-1);

		try {

			mongo = new MongoBase();
			mongo.setDB(Constants.ANALYTICS_DB);
			mongo.setCollection(Constants.INFLUENCE_COLLECTION);
			collection = mongo.getCollection();
			cursor = collection.find(query, fields);
			cursor = cursor.sort(orderBy).limit(n);

			while(cursor.hasNext()) { 

				topList.add((String) cursor.next().get("UserName"));
			}
			
		} catch (UnknownHostException e) { 

			e.printStackTrace();
		}

		return topList;
	}
	
	/** 
	 * Returns a map containing names of top N users and their score
	 * @param n Integer containing the value of N
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Map<String,Double> containing the mapping between the top N users
	 */
	public static Map<String,Double> getTopUsers(int n,Date from,Date to) { 

		DBObject query;
		DBObject projectFields;
		DBObject project;
		DBObject orderBy;
		DBObject match;
		DBObject groupFields;
		DBObject group;
		DBObject limit;
		DBCollection collection;
		DBObject temp;
		MongoBase mongo;
		Map<String,Double> topMap = new HashMap<String,Double>();

		query = new BasicDBObject("Channel","Instagram").
				append("LastUpdated", new BasicDBObject("$gte",from).append("$lte", to));
		match = new BasicDBObject("$match",query);

		projectFields = new BasicDBObject("UserName",1).append("Index", 1).append("_id", 0);
		project = new BasicDBObject("$project",projectFields); 
		
		groupFields = new BasicDBObject("_id","$UserName").
				append("averageIndex", new BasicDBObject("$avg","$Index"));
		group = new BasicDBObject("$group",groupFields); 
		
		orderBy = new BasicDBObject("$sort",new BasicDBObject("averageIndex",-1));
		limit = new BasicDBObject("$limit",n);

		try {

			mongo = new MongoBase();
			mongo.setDB(Constants.ANALYTICS_DB);
			mongo.setCollection(Constants.INFLUENCE_COLLECTION); 
			collection = mongo.getCollection();
			List<DBObject> pipeline = Arrays.asList(match,project,group,orderBy,limit);
			AggregationOutput output = collection.aggregate(pipeline); 
			
			if(output.results().iterator().hasNext()) {  
				
				Iterator<DBObject> iter = output.results().iterator(); 
				
				while(iter.hasNext()) {  
					
					temp = iter.next();
					topMap.put((String)temp.get("_id"), (Double)temp.get("averageIndex"));
				}
			}
			
		} catch (Exception e) {  

			e.printStackTrace();
		}

		return topMap;
	}
	
	public static void main(String args[]) throws UnknownHostException { 
		
		DateTime to = new DateTime(); 
		DateTime from = to.minusDays(3); 
		
		System.out.println(getTopUsers(10,from.toDate(),to.toDate()));
		
		System.out.println(MongoDBUtils.getMaxMinValue("Analytics", "Influence", "Index", "max"));
	}
}
