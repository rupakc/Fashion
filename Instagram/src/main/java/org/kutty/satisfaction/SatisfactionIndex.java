package org.kutty.satisfaction;

import java.util.Date;

import org.joda.time.DateTime;
import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/** 
 * Calculates the Satisfaction Index of a given product and stores it in the database
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 15 September, 2015 
 * 
 * TODO - Add reliability and frequency weights (functions and code)
 * TODO - Add database support 
 */ 

public class SatisfactionIndex {
	
	/** 
	 * Returns the label count for a given product and channel in a given interval
	 * @param channel String containing the channel name
	 * @param brand String containing the brand name
	 * @param type String containing the type (i.e. "sentiment" or "spam")
	 * @param label String containing the label
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Integer containing the count
	 */ 
	
	public static int getLabelCount(String channel,String brand,String type,String label,Date from,Date to) { 

		int count = 0;
		DBObject query = null;
		MongoBase mongo;
		DBCollection collection;
		double fromDate;
		double toDate; 

		try { 

			if (type.equalsIgnoreCase(Constants.SENTIMENT_TYPE)) { 

				if (!channel.equalsIgnoreCase("Instagram")) {

					query = new BasicDBObject("Channel",channel).append("Product",brand).
							append("SentimentLabel",label).
							append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));

				} else { 

					fromDate = DateConverter.getJulianDate(from);
					toDate = DateConverter.getJulianDate(to); 

					query = new BasicDBObject("Channel",channel).append("Product",brand).
							append("SentimentLabel",label).
							append("OtherDate", new BasicDBObject("$gte",fromDate).append("$lte",toDate));
				}
			}  

			if (type.equalsIgnoreCase(Constants.SPAM_TYPE)) { 

				if (!channel.equalsIgnoreCase("Instagram")) {

					query = new BasicDBObject("Channel",channel).append("Product",brand).
							append("SpamLabel",label).
							append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));

				} else { 

					fromDate = DateConverter.getJulianDate(from);
					toDate = DateConverter.getJulianDate(to); 

					query = new BasicDBObject("Channel",channel).append("Product",brand).
							append("SpamLabel",label).
							append("OtherDate", new BasicDBObject("$gte",fromDate).append("$lte",toDate));
				}

			}

			mongo = new MongoBase(); 
			mongo.setDB("Analytics");
			mongo.setCollection(brand);
			collection = mongo.getCollection();
			count = collection.find(query).size(); 

		} catch (Exception e) {  

			e.printStackTrace();
		} 

		return count; 
	}
	
	/** 
	 * Returns the count of post for a given channel and a given brand
	 * @param channel String containing the channel name
	 * @param brand String containing the brand name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Integer containing the count of posts
	 */ 
	
	public static int getPostCount(String channel,String brand,Date from, Date to) { 

		int count = 0;

		DBObject query = null;
		MongoBase mongo;
		DBCollection collection;
		double fromDate;
		double toDate; 

		try { 

			if (!channel.equalsIgnoreCase("Instagram")) {

				query = new BasicDBObject("Channel",channel).append("Product",brand).
						append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));

			} else { 

				fromDate = DateConverter.getJulianDate(from);
				toDate = DateConverter.getJulianDate(to); 

				query = new BasicDBObject("Channel",channel).append("Product",brand).
						append("OtherDate", new BasicDBObject("$gte",fromDate).append("$lte",toDate));
			}

			mongo = new MongoBase(); 
			mongo.setDB("Analytics");
			mongo.setCollection(brand);
			collection = mongo.getCollection();
			count = collection.find(query).size(); 

		} catch (Exception e) {  

			e.printStackTrace();
		}

		return count;
	}
	
	/** 
	 * Calculates the reliability factor for a given channel and a given brand
	 * @param channel String containing the channel name
	 * @param brand String containing the brand name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Double containing the value of the factor
	 */ 
	
	public static double getReliabilityFactor(String channel,String brand,Date from,Date to) { 
		
		double reliabitityFactor = 0.0; 
		double spread;
		double ham;
		double negative;
		double positive;
		double neutral;
		
		ham = getLabelCount(channel, brand,Constants.SPAM_TYPE,Constants.HAM_LABEL, from, to);
		negative = getLabelCount(channel, brand, Constants.SENTIMENT_TYPE,Constants.NEGATIVE_LABEL, from, to);
		positive = getLabelCount(channel, brand, Constants.SENTIMENT_TYPE, Constants.POSITIVE_LABEL, from, to);
		neutral = getLabelCount(channel, brand, Constants.SENTIMENT_TYPE,Constants.NEUTRAL_LABEL, from, to);
		
		spread = Math.abs(positive-negative) + Math.abs(positive-neutral) + Math.abs(neutral-negative) + 1;
		
		reliabitityFactor = ham/spread;
		
		return reliabitityFactor;
	}
	
	/** 
	 * Returns the frequency factor of a given channel and brand
	 * @param channel String containing the channel name
	 * @param brand String containing the brand name
	 * @param meanRetro Integer containing the number of retro days for which mean has to be calculated
	 * @param postRetro Integer containing the number of retro post count
	 * @return Double containing the value of the frequency factor
	 */ 
	
	public static double getFrequencyFactor(String channel,String brand, int meanRetro,int postRetro) { 
		
		double frequencyFactor = 0.0;
		double postRate;
		double meanRate;
		
		DateTime to = new DateTime();
		DateTime from = to.minusDays(postRetro);
		
		postRate = getPostCount(channel, brand, from.toDate(), to.toDate())/postRetro;
		from = to.minusDays(meanRetro);
		meanRate = getPostCount(channel, brand, from.toDate(), to.toDate())/meanRetro;
		
		frequencyFactor = (postRate/(meanRate+1));
		
		return frequencyFactor;
	}
	
	/** 
	 * Given a channel and a brand returns the satisfaction index associated with it 
	 * @param channel String containing the channel name
	 * @param brand String containing the brand name
	 * @return Double containing the satisfaction index value
	 */ 
	
	public static double getSatisfactionIndex(String channel,String brand) { 
		
		double frequencyFactor;
		double reliabilityFactor;
		
		DateTime to = new DateTime();
		DateTime from = to.minusDays(Constants.DAYS);
		
		reliabilityFactor = getReliabilityFactor(channel, brand, from.toDate(), to.toDate());
		frequencyFactor = getFrequencyFactor(channel, brand, Constants.MEAN_DAYS,Constants.DAYS);
		
		return (reliabilityFactor + frequencyFactor);
	}
}
