package org.kutty.sentiment;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.classification.EnsembleMachineSentiment;
import org.kutty.db.MongoBase;
import org.kutty.dbo.Sentiment;
import org.kutty.utils.DateConverter;
import org.kutty.utils.ListConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Detects the sentiment of a given post (i.e. Positive, Negative, Neutral)
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 20 September, 2015
 *
 */

public class SentimentDetector {

	public static String [] channelNames = {"Twitter","Facebook","Instagram","Youtube"};
	public static String [] brandNames = {"Forever21","FreePeople","Guess","HandM","Levis","Mango",
		"RagandBone","SevenForAllMankind","TrueReligion"};
	private EnsembleMachineSentiment ems = new EnsembleMachineSentiment();
	
	/** 
	 * Defines the pipeline for sentiment detection for all posts of a given channel and product
	 * @param brandName String containing the brand name
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 
	
	public void sentimentPipeline(String brandName,String channel,Date from, Date to) { 

		Set<DBObject> dataSet = getSentimentSet(brandName, channel, from, to);
		Sentiment sentiment;
		String text;
		String tagSet = null;
		String label; 
		MongoBase mongo; 

		for (DBObject temp : dataSet) { 

			sentiment = new Sentiment();
			sentiment.setChannel(channel);
			sentiment.setProduct(brandName); 

			if (!channel.equalsIgnoreCase("Instagram")) {

				text = (String) temp.get("Message");

			} else { 

				text = (String) temp.get("CaptionText");
				tagSet = ListConverter.getCSVSet((BasicDBList) temp.get("TagSet"));
			} 

			if (channel.equalsIgnoreCase("Instagram") || channel.equalsIgnoreCase("Youtube")) { 

				sentiment.setAuthor((String) temp.get("Author"));

			} else { 

				sentiment.setAuthor((String) temp.get("UserName"));
			}

			if (!channel.equalsIgnoreCase("Instagram")) { 

				sentiment.setTimestamp((Date) temp.get("TimeStamp")); 

			} else { 

				sentiment.setOtherDate((Double) temp.get("Timestamp"));
			}

			if (!channel.equalsIgnoreCase("Instagram")) { 

				label = getSentimentLabelOtherChannels(text, channel);

			} else { 

				label = getSentimentLabelInstagram(text,tagSet);
			}

			sentiment.setSentimentLabel(label);
			sentiment.setContent(text);
			sentiment.setUpdateModels(ems.CORRECT_UPDATES);
			System.out.println(label);
			try {
				
				mongo = new MongoBase();
				mongo.setDB("Analytics");
				mongo.setCollection(brandName);
				mongo.putInDB(sentiment); 
				
			} catch(Exception e) { 
				
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Carries out sentiment analysis for all channels of a given brand
	 * @param brandName String containing the brandName
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 
	
	public void sentimentForAllChannels(String brandName,Date from,Date to) { 
		
		for (String channelName : channelNames) { 
			
			sentimentPipeline(brandName, channelName, from, to);
		}
	}
	
	/** 
	 * Carries out sentiment analysis for all products of a given channel
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 
	
	public void sentimentForAllProducts(String channel,Date from,Date to) { 
		
		for (String brandName : brandNames) { 
			
			sentimentPipeline(brandName, channel, from, to);
		}
	}
	
	/** 
	 * Carries out sentiment analysis for all products and channels
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 
	
	public void sentimentForAll(Date from,Date to) { 
		
		for (String brandName : brandNames) { 
			
			for (String channel : channelNames) { 
				
				sentimentPipeline(brandName, channel, from, to);
			}
		}
	}
	
	/** 
	 * Returns the sentiment objects from the database
	 * @param collectionName String containing the collection name
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Set<DBObject> containing the set of database objects
	 */ 
	
	public Set<DBObject> getSentimentSet(String collectionName,String channel,Date from,Date to) {

		Set<DBObject> dataSet = new HashSet<DBObject>();

		try {

			MongoBase mongo = new MongoBase();
			mongo.setCollection(collectionName);
			double fromDate;
			double toDate;
			DBObject query;
			DBCollection collection;
			DBCursor cursor;
			BasicDBList queryList; 

			if (channel.equalsIgnoreCase("Instagram")) { 

				queryList = new BasicDBList();
				queryList.add(new BasicDBObject("Type", "tag"));
				queryList.add(new BasicDBObject("Type", "image"));
				queryList.add(new BasicDBObject("Type", "video"));

				fromDate = DateConverter.getJulianDate(from);
				toDate = DateConverter.getJulianDate(to); 

				query = new BasicDBObject("Channel","Instagram").append("$or", queryList).
						append("Timestamp", new BasicDBObject("$gte",fromDate).append("$lte", toDate));
			} else { 
				
				query = new BasicDBObject("Channel",channel).
						append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
			}

			collection = mongo.getCollection();
			cursor = collection.find(query);

			while(cursor.hasNext()) { 

				dataSet.add(cursor.next());
			}

		} catch (UnknownHostException e) { 
			
			e.printStackTrace();
		}

		return dataSet;
	}
	
	/** 
	 * Returns the sentiment label for a given text and channel
	 * @param text String containing the text which is to be classified
	 * @param channelName String containing the channel name
	 * @return String containing the sentiment label (i.e. positive, negative, neutral)
	 */ 
	
	public String getSentimentLabelOtherChannels(String text,String channelName) { 

		return ems.organizeAndActEnsemble(text, channelName, 5);
	}
	
	/** 
	 * Returns the sentiment label for a Instagram post
	 * @param captionText String containing the captionText
	 * @param tagSet String containing the tagSet
	 * @return String containing the sentiment label (i.e. positive,negative,neutral)
	 */ 
	
	public String getSentimentLabelInstagram(String captionText,String tagSet) { 

		return ems.organizeAndActEnsemble(captionText, tagSet,"Instagram",5);
	}
	
	public static void main(String args[]) { 

		SentimentDetector sd = new SentimentDetector();
		DateTime to = new DateTime();
		DateTime from = to.minusDays(2);
		sd.sentimentPipeline("Levis","Twitter", from.toDate(), to.toDate());
	}
}
