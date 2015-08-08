package org.kutty.analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;
import org.kutty.utils.LanguageDetector;

import com.cybozu.labs.langdetect.LangDetectException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Profiles posts on the basis of their language of origin i.e. English, German French etc 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 5 August, 2015
 * 
 * TODO - Perform Analytics for All products and all channels
 */

public class LanguageAnalytics {

	public static Map<String,String> language_code_map = new HashMap<String,String>(); 
	public static Map<String, String> collection_names = new HashMap<String,String>();;

	static { 

		try { 
			loadLanguageCodes("language_map.txt",language_code_map);
			LanguageDetector.init("profiles"); 

			init("product_list.txt",collection_names);
		} catch (IOException | LangDetectException e) {
			e.printStackTrace();
		}
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
	 * @param filename
	 * @param language
	 * @throws IOException
	 */ 
	
	public static void loadLanguageCodes(String filename,Map<String,String> language) throws IOException { 

		BufferedReader br;
		FileReader fr;
		String temp = ""; 
		int index = -1; 
		String code = "";
		String name = ""; 

		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		while((temp = br.readLine()) != null) { 

			index = temp.indexOf('\t');

			if (index != -1) { 

				code = temp.substring(0,index).trim();
				name = temp.substring(index+1).trim();
				language.put(code, name);
			}
		}

		br.close();
		fr.close();
	}
	
	/** 
	 * 
	 * @param s
	 * @return
	 */ 
	
	public static String getLanguage(String s) { 

		String name = ""; 

		try { 

			String code = LanguageDetector.detect(s);
			name = language_code_map.get(code); 

		} catch (LangDetectException e) { 

			e.printStackTrace();
		} 

		return name;
	} 
	
	/** 
	 * 
	 * @param product
	 * @param channel
	 * @param from
	 * @param to
	 * @return
	 */ 
	
	public static Map<String,Integer> getLanguageProductChannel(String product,String channel,Date from,Date to) {

		MongoBase mongo = null;
		BasicDBObject query;
		BasicDBObject fields;
		DBCollection collection;
		DBCursor cursor;
		DBObject temp;
		double from_date;
		double to_date;
		String collection_name = ""; 
		String text = ""; 
		String language = ""; 
		
		from_date = DateConverter.getJulianDate(from);
		to_date = DateConverter.getJulianDate(to);
		collection_name = collection_names.get(product.toLowerCase().trim()); 
		Map<String,Integer> language_count_map = new HashMap<String,Integer>(); 
		
		if (!(channel.equalsIgnoreCase("Reddit") || channel.equalsIgnoreCase("Instagram"))) { 

			query = new BasicDBObject("Channel",channel).
					append("TimeStamp", new BasicDBObject("$gte",from).append("$lte",to));
			fields = new BasicDBObject("Message",1).append("TimeStamp", 1); 

		} else { 

			if (channel.equalsIgnoreCase("Reddit")) { 

				query = new BasicDBObject("Channel",channel).
						append("TimeStamp", new BasicDBObject("$gte",from_date).append("$lte",to_date));

				fields = new BasicDBObject("Message",1).append("TimeStamp",1); 

			} else { 

				BasicDBList query_list = new BasicDBList();
				query_list = new BasicDBList();
				query_list.add(new BasicDBObject("Type", "tag"));
				query_list.add(new BasicDBObject("Type", "image"));
				query_list.add(new BasicDBObject("Type", "video"));

				query = new BasicDBObject("Channel",channel).append("$or",query_list).
						append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte",to_date)); 

				fields = new BasicDBObject("CaptionText",1).append("Timestamp",1);
			}	
		}

		try {  

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields); 

			while(cursor.hasNext()) { 

				temp = cursor.next(); 
				
				if (!channel.equalsIgnoreCase("Instagram")) { 
					
					text = (String) temp.get("Message");  
					
				} else { 
					
					text = (String) temp.get("CaptionText");
				}
				
				language = getLanguage(text);
				
				if (language_count_map.containsKey(language)) { 
					
					int count = language_count_map.get(language);
					language_count_map.put(language, count+1); 
					
				} else {  
					
					language_count_map.put(language,1);
				}
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} finally { 

			if (mongo != null) {  

				mongo.closeConnection();
			}
		} 

		return language_count_map;
	} 
	
	/** 
	 * 
	 * @param args
	 */ 
	
	public static void main(String args[]) { 
		try {

			System.out.println(language_code_map.size());
			System.out.println(getLanguage("Hello My friend we meet again"));
			DateTime now = new DateTime();
			DateTime prev = now.minusYears(3);
			System.out.println(getLanguageProductChannel("gueSS", "Twitter",prev.toDate(),now.toDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
