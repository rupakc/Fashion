package org.kutty.analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/** 
 * Analyzes the meta data of the database useful for generating overall statistics 
 * 
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 5 August, 2015
 */

public class MetaAnalytics {

	private static String channel_name;
	private static String product_name;
	private static Date start_date;
	private Date end_date; 
	public static Map<String,String> collection_names = new HashMap<String,String>();
	public static List<String> channel_list = new ArrayList<String>();

	/** 
	 * Static block to initialize the product list and collection name
	 */ 

	static { 

		channel_list.add("Twitter");
		channel_list.add("Facebook");
		channel_list.add("Youtube");
		channel_list.add("Reddit");
		channel_list.add("Instagram");

		try {
			init("product_list.txt",collection_names);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  
	
	/** 
	 * 
	 * @return
	 */ 
	
	public static String getChannelName() { 
		
		return channel_name;
	}
	
	/** 
	 * 
	 * @param channel_name
	 */ 
	
	public static void setChannelName(String channel_name) { 
		
		MetaAnalytics.channel_name = channel_name;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public static String getProductName() { 
		
		return product_name;
	}
	
	/** 
	 * 
	 * @param product_name
	 */ 
	
	public static void setProductName(String product_name) { 
		
		MetaAnalytics.product_name = product_name;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public static Date getStartDate() { 
		
		return start_date;
	}
	
	/** 
	 * 
	 * @param start_date
	 */ 
	
	public static void setStartDate(Date start_date) { 
		
		MetaAnalytics.start_date = start_date;
	}
	
	/** 
	 * 
	 * @return
	 */ 
	
	public Date getEndDate() { 
		
		return end_date;
	}
	
	/** 
	 * 
	 * @param end_date
	 */ 
	
	public void setEndDate(Date end_date) { 
		
		this.end_date = end_date;
	}

	/** 
	 * Utility function to initialize the list of fashion brands and their associated collection names
	 * @param filename String containing the filename from which the data is to be read
	 * @throws IOException
	 */ 

	public static void init(String filename,Map<String,String> name_map) throws IOException
	{ 
		BufferedReader br;
		FileReader fr;
		String alias;
		String collection_name;
		String s = ""; 
		int index; 

		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		while((s = br.readLine()) != null) { 

			index = s.indexOf('=');

			if(index != -1) { 

				alias = s.substring(0,index);
				collection_name = s.substring(index+1,s.length());
				alias = alias.trim();
				collection_name = collection_name.trim();
				name_map.put(alias, collection_name); 
			}
		}

		br.close();
		fr.close();
	}   
	
	/** 
	 * 
	 * @param product
	 * @param channel
	 * @param from
	 * @param to
	 * @return
	 */ 
	
	public static int getProductChannelPostCount(String product,String channel,Date from,Date to) { 

		MongoBase mongo = null;
		BasicDBObject query;
		DBCollection collection;
		double from_date;
		double to_date;
		String collection_name = ""; 
		int size = 0; 

		from_date = DateConverter.getJulianDate(from);
		to_date = DateConverter.getJulianDate(to);
		collection_name = collection_names.get(product.toLowerCase().trim()); 

		if (!(channel.equalsIgnoreCase("Reddit") || channel.equalsIgnoreCase("Instagram"))) { 

			query = new BasicDBObject("Channel",channel).
					append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
		} else { 

			if (channel.equalsIgnoreCase("Reddit")) { 

				query = new BasicDBObject("Channel",channel).
						append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
			} else { 

				BasicDBList query_list = new BasicDBList();
				query_list = new BasicDBList();
				query_list.add(new BasicDBObject("Type", "tag"));
				query_list.add(new BasicDBObject("Type", "image"));
				query_list.add(new BasicDBObject("Type", "video"));

				query = new BasicDBObject("Channel",channel).append("$or",query_list).
						append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
			}	
		}

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			size = collection.find(query).size();

		} catch (Exception e) {  

			e.printStackTrace(); 

		} finally { 

			if (mongo != null) {  

				mongo.closeConnection();
			}
		}

		return size;
	}
	
	/** 
	 * 
	 * @param product
	 * @return
	 */ 
	
	public static int getCollectionSize(String product) { 

		int size;
		DBCollection collection;
		String collection_name = "";
		MongoBase mongo = null; 

		collection_name = collection_names.get(product.toLowerCase().trim());
		size = 0; 

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection();
			size = collection.find().size(); 

		} catch (Exception e) { 

			e.printStackTrace(); 

		} finally { 

			if (mongo != null) {  

				mongo.closeConnection();
			}
		}

		return size;
	} 
	
	/** 
	 * 
	 * @param product
	 * @param from
	 * @param to
	 * @return
	 */ 
	
	public static int getCollectionSize(String product,Date from,Date to) { 

		int size;
		int size_reddit;
		int size_instagram;
		DBCollection collection;
		String collection_name = "";
		MongoBase mongo = null; 
		double from_date;
		double to_date;  

		collection_name = collection_names.get(product.toLowerCase().trim());
		BasicDBList query_list = new BasicDBList();
		List<String> channel_list = new ArrayList<String>(); 
		BasicDBObject query; 
		from_date = DateConverter.getJulianDate(from);
		to_date = DateConverter.getJulianDate(to); 
		size = 0;
		size_reddit = 0;
		size_instagram = 0;

		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video")); 

		channel_list.add("Twitter");
		channel_list.add("Facebook");
		channel_list.add("Youtube");

		query = new BasicDBObject("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to)).
				append("Channel", new BasicDBObject("$in",channel_list));

		try {  

			mongo = new MongoBase(); 
			mongo.setCollection(collection_name);
			collection = mongo.getCollection();  

			size = collection.find(query).size(); 

			query = new BasicDBObject("Channel","Reddit").
					append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

			size_reddit = collection.find(query).size(); 

			query = new BasicDBObject("Channel","Instagram").append("$or",query_list).
					append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date));

			size_instagram = collection.find(query).size();

		} catch (Exception e) {  

			e.printStackTrace(); 

		} finally { 

			if (mongo != null) { 

				mongo.closeConnection();
			}
		}

		return (size + size_reddit + size_instagram);
	} 
	
	/** 
	 * 
	 * @param product
	 * @param from
	 * @param to
	 * @return
	 */ 
	
	public static Map<String,Integer> getAllChannelCount(String product,Date from,Date to) { 

		String collection_name = "";
		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		int size = 0;
		MongoBase mongo = null;
		DBCollection collection;
		BasicDBList query_list = new BasicDBList(); 
		BasicDBObject query;
		Map<String,Integer> channel_count = new HashMap<String,Integer>(); 
		collection_name = collection_names.get(product.toLowerCase().trim()); 

		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video")); 

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 

			for (String channel : channel_list) { 

				if (!(channel.equalsIgnoreCase("Reddit") || channel.equalsIgnoreCase("Instagram"))) { 
					
					query = new BasicDBObject("Channel",channel).
							append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to)); 
					
				} else if (channel.equalsIgnoreCase("Reddit")) { 
					
					query = new BasicDBObject("Channel",channel).
							append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
				} else { 
					
					query = new BasicDBObject("Channel",channel).append("$or",query_list).
							append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));
				}
				
				size = collection.find(query).size();
				channel_count.put(channel, size);
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} finally {  

			if (mongo != null) { 

				mongo.closeConnection();
			}
		}
		
		return channel_count;
	} 
	
	/** 
	 * 
	 * @param args
	 */ 
	
	public static void main(String args[]) { 

		DateTime now = new DateTime();
		DateTime prev = now.minusYears(3);
		System.out.println(getProductChannelPostCount("gueSS", "Instagram",prev.toDate(),now.toDate()));
		System.out.println(getCollectionSize("Guess"));
		System.out.println(getCollectionSize("gueSs",prev.toDate(),now.toDate()));
		System.out.println(getAllChannelCount("Guess",prev.toDate(),now.toDate()));
	}
}
