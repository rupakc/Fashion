package org.kutty.giveaway;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.classification.EnsembleMachineGiveaway;
import org.kutty.db.MongoBase;
import org.kutty.dbo.Giveaway;
import org.kutty.utils.DateConverter;
import org.kutty.utils.ListConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Class for Giveaway detection pipeline
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 26 August, 2015 
 * 
 */ 

public class GiveawayDetection {
	
	/** 
	 * Defines the pipeline for processing of Instagram Giveaways
	 * @param collection_name String containing the collection name from which the data is to be collected
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 
	
	public void GiveawayProcessingPipeline(String collection_name,Date from,Date to) { 
		
		Giveaway give;
		Set<DBObject> data_set = getGiveAwayData(collection_name, from, to);
		String captionText;
		String tagSet; 
		String classLabel;
		String username; 
		double timestamp; 
				
		for (DBObject temp : data_set) { 
			
			give = new Giveaway();
			captionText = (String) temp.get("CaptionText");
			tagSet = ListConverter.getCSVSet((BasicDBList) temp.get("TagSet"));
			timestamp = (double) temp.get("Timestamp");
			classLabel = getClassLabel(captionText, tagSet);
			username = (String) temp.get("Username"); 
			
			give.setCaptionText(captionText);
			give.setClassLabel(classLabel);
			give.setTagSet(tagSet);
			give.setTimeStamp(timestamp);
			give.setUserName(username);
			
			putInDB(give,"Analytics","Giveaway");
		}
	}
	
	/** 
	 * Inserts a given giveaway object in the database
	 * @param give Giveaway object which is to be inserted in the database
	 * @param db_name String containing the database name into which it is to be inserted
	 */ 
	
	public void putInDB(Giveaway give,String db_name,String collection_name) { 
		
		MongoBase mongo = null;
		
		try { 
			
			mongo = new MongoBase(); 
			mongo.setDB(db_name);
			mongo.setCollection(collection_name);
			mongo.putInDB(give); 
			
		} catch (Exception e) {  
			
			e.printStackTrace(); 
			
		} finally { 
			
			if (mongo != null) { 
				
				mongo.closeConnection();
			}
		}
	} 
	
	/** 
	 * Returns the class label for a given caption text and tagset
	 * @param captionText String containing the caption text
	 * @param tagSet String containing the tagset
	 * @return String containing the class label derived from ensemble learning
	 */ 
	
	public String getClassLabel(String captionText,String tagSet) { 
		
		String label = "";
		EnsembleMachineGiveaway em = new EnsembleMachineGiveaway();
		label = em.organizeAndActEnsemble(captionText, tagSet, 5);
		
		return label;
	} 
	
	/** 
	 * Given a time interval returns the giveaway data between the dates
	 * @param collection_name String containing the collection name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Set<DBObject> containing the collection of giveaway data
	 */ 
	
	public Set<DBObject> getGiveAwayData(String collection_name,Date from,Date to) { 
		
		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<DBObject> data_set = new HashSet<DBObject>();
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
			
			while(cursor.hasNext()) { 
				
				temp = cursor.next();
				data_set.add(temp);
			} 
			
		} catch (Exception e) {  
			
			e.printStackTrace(); 
			
		} finally { 
			
			if (mongo != null) { 
				
				mongo.closeConnection();
			}
		}
		
		return data_set;
	}
	
	public static void main(String args[]) { 
		
		GiveawayDetection gd = new GiveawayDetection();
		DateTime to = new DateTime();
		DateTime from = to.minusYears(5);
		gd.GiveawayProcessingPipeline("Giveaway", from.toDate(), to.toDate());
	}
}
