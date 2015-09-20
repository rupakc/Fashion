package org.kutty.spam;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.classification.EnsembleMachineSpam;
import org.kutty.db.MongoBase;
import org.kutty.dbo.Spam;
import org.kutty.utils.DateConverter;
import org.kutty.utils.ListConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Detects the spamicity of a given post (i.e. Spam or Ham)
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 20 September, 2015
 * 
 */

public class SpamDetector {

	public static String [] channelNames = {"Twitter","Facebook","Instagram","Youtube"};
	public static String [] brandNames = {"Forever21","FreePeople","Guess","HandM","Levis","Mango",
		"RagandBone","SevenForAllMankind","TrueReligion"}; 

	/** 
	 * Defines a spam detection pipeline for a given brand and a given channel
	 * @param brandName String containing the brand name
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 

	public void spamPipeline(String brandName,String channel,Date from, Date to) { 

		Set<DBObject> dataSet = getSpamSet(brandName, channel, from, to);
		Spam spam;
		String text;
		String tagSet = null;
		String label; 
		MongoBase mongo; 

		for (DBObject temp : dataSet) { 

			spam = new Spam();
			spam.setChannel(channel);
			spam.setProduct(brandName); 

			if (!channel.equalsIgnoreCase("Instagram")) {

				text = (String) temp.get("Message");

			} else { 

				text = (String) temp.get("CaptionText");
				tagSet = ListConverter.getCSVSet((BasicDBList) temp.get("TagSet"));
			} 

			if (channel.equalsIgnoreCase("Instagram") || channel.equalsIgnoreCase("Youtube")) { 

				spam.setAuthor((String) temp.get("Author"));

			} else { 

				spam.setAuthor((String) temp.get("UserName"));
			}

			if (!channel.equalsIgnoreCase("Instagram")) { 

				spam.setTimestamp((Date) temp.get("TimeStamp")); 

			} else { 

				spam.setOtherDate((Double) temp.get("Timestamp"));
			}

			if (!channel.equalsIgnoreCase("Instagram")) { 

				label = getSpamLabelOtherChannels(text, channel);

			} else { 

				label = getSpamLabelInstagram(text,tagSet);
			}

			spam.setSpamLabel(label);
			spam.setContent(text);
			System.out.println(label + " : " + text);
			try {

				mongo = new MongoBase();
				mongo.setDB("Analytics");
				mongo.setCollection(brandName);
				mongo.putInDB(spam); 

			} catch(Exception e) { 

				e.printStackTrace();
			}
		}
	}

	/** 
	 * Carries out spam analysis for all channels of a given brand
	 * @param brandName String containing the brandName
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 

	public void spamForAllChannels(String brandName,Date from,Date to) { 

		for (String channelName : channelNames) { 

			spamPipeline(brandName, channelName, from, to);
		}
	}

	/** 
	 * Carries out spam analysis for all products of a given channel
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 

	public void spamForAllProducts(String channel,Date from,Date to) { 

		for (String brandName : brandNames) { 

			spamPipeline(brandName, channel, from, to);
		}
	}

	/** 
	 * Carries out spam detection for all products and channels
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 */ 

	public void spamForAll(Date from,Date to) { 

		for (String brandName : brandNames) { 

			for (String channel : channelNames) { 

				spamPipeline(brandName, channel, from, to);
			}
		}
	}

	/** 
	 * Returns DBObjects to be classified as spam/ham
	 * @param collectionName String containing the collection name
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Set<DBObject> containing the set of objects retrieved from database
	 */ 

	public Set<DBObject> getSpamSet(String collectionName,String channel,Date from,Date to) {

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
	 * Detects the spamicity of a given post for all other channels
	 * @param text String containing the text which is to be classified
	 * @param channelName String containing the channel name
	 * @return String containing the spam label
	 */ 

	public String getSpamLabelOtherChannels(String text,String channelName) { 

		EnsembleMachineSpam ems = new EnsembleMachineSpam();
		return ems.organizeAndActEnsemble(text, channelName, 5);
	}

	/** 
	 * Returns the spam label for Instagram
	 * @param captionText String containing the caption text
	 * @param tagSet String containing the tagset
	 * @return String containing the spam label (i.e spam or ham)
	 */ 

	public String getSpamLabelInstagram(String captionText,String tagSet) { 

		EnsembleMachineSpam ems = new EnsembleMachineSpam();
		return ems.organizeAndActEnsemble(captionText, tagSet,"Instagram",5);
	}

	public static void main(String args[]) { 

		SpamDetector sd = new SpamDetector();
		DateTime to = new DateTime();
		DateTime from = to.minusYears(2);
		sd.spamPipeline("Forever21","Twitter", from.toDate(), to.toDate());
	}
}
