package org.kutty.analytics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.kutty.db.MongoBase;
import org.kutty.utils.DateConverter;
import org.kutty.utils.ListConverter;
import org.kutty.utils.PrintUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/** 
 * Performs basic analytics on the Instagram tags and associated content stored in the db
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 18 July, 2015 
 *  
 *  TODO - Add user analytics,like and comment count (between dates)
 */ 

public class InstagramAnalytics {

	public static String product_name;
	public static String collection_name;
	public static Map<String,String> collection_names = new HashMap<String,String>();

	/** 
	 * Static block to initialize the product list and collection name
	 */ 

	static { 

		try {
			init("product_list.txt",collection_names);
		} catch (IOException e) {
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
	 * Returns a set of most recent tag captions for a given product and a given time interval 
	 * @param product_name String containing the product name which is to be searched
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of results to be returned
	 * @return Set<String> containing the set of most recent caption associated with a tag
	 */ 

	public static Set<String> getMostRecentTagCaptions(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null ; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

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
			cursor = cursor.sort(new BasicDBObject("Timestamp",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("CaptionText"));
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
		return tag_set;
	}

	/** 
	 * Returns the caption text of a set of most liked tags during a given time interval
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of results to be fetched
	 * @return Set<String> containing the caption text of most liked posts
	 */ 

	public static Set<String> getMostLikedTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("CaptionText",1).append("LikeCount", 1).append("Timestamp", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				System.out.println(temp.get("LikeCount"));
				tag_set.add((String) temp.get("CaptionText"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} 

		return tag_set;
	}

	/** 
	 * Returns a set of caption text corresponding to most liked tags
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of top comments
	 * @return Set<String> containing the caption text of the top commented posts
	 */ 

	public static Set<String> getMostCommentedTags(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("CaptionText",1).append("CommentCount", 1).append("Timestamp", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("CommentCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("CaptionText"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
		return tag_set;
	} 

	/** 
	 * Returns a set of caption text corresponding to most commented tags
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing the number of top comments
	 * @return Set<String> containing the caption text of the top commented posts
	 */ 

	public static Set<String> getMostPopularTagLinks(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("CaptionText",1).append("CommentCount", 1).append("Timestamp", 1).
				append("Link", 1).append("LikeCount", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("Link"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
		return tag_set;
	}  

	/** 
	 * Returns a set of most popular image links
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing number of top links to display
	 * @return Set<String> containing the most poplular image links
	 */ 

	public static Set<ArrayList<String>> getMostPopularTags(String product_name,Date from,Date to,int n) {

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<ArrayList<String>> tag_set = new HashSet<ArrayList<String>>();
		ArrayList<String> temp_tag; 
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		BasicDBList tag_list; 
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("TagSet",1).append("LikeCount",1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name); 
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields); 

			while(cursor.hasNext()) { 

				tag_list = (BasicDBList) cursor.next().get("TagSet");
				temp_tag = ListConverter.getArrayListString(tag_list);
				tag_set.add(temp_tag);
			}

		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
		return tag_set;
	} 

	/** 
	 * Returns a Map of country and its associated count 
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Map<String,Integer> containing the mapping between the corresponding country and its count
	 */ 

	public static Map<String,Integer> getCountryMap(String product_name,Date from,Date to) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Map<String,Integer> country_map = new HashMap<String,Integer>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		String country = "";
		int country_count = 0; 

		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("Country",1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields);

			while (cursor.hasNext()) { 

				temp = cursor.next();
				country = (String) temp.get("Country");

				if (!country.isEmpty()) { 

					if (country_map.containsKey(country)) { 

						country_count = country_map.get(country);
						country_map.put(country, country_count+1); 

					} else { 

						country_map.put(country, 1);
					}
				}
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return country_map;
	} 

	/** 
	 * Returns a set of most popular image links
	 * @param product_name String containing the product name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @param n Integer containing number of top links to display
	 * @return Set<String> containing the most popular image links
	 */ 

	public static Set<String> getMostPopularImageLinks(String product_name,Date from,Date to,int n) { 

		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Set<String> tag_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("ImageLink",1).append("LikeCount", 1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			cursor = cursor.sort(new BasicDBObject("LikeCount",-1)).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				tag_set.add((String) temp.get("ImageLink"));
			} 

		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
		return tag_set;
	} 
	
	/** 
	 * Returns the most recent N comments on a particular tag
	 * @param product String containing the product name
	 * @param tagId String containing the tagId
	 * @param n Integer containing the count of comments to be fetched
	 * @return Set<String> containing the comments
	 */ 
	
	public static Set<String> getCommentSetForTag(String product,String tagId,int n) { 

		Set<String> comment_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		DBObject temp;
		DBObject sort_by;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product.toLowerCase().trim()); 

		query = new BasicDBObject("Channel","Instagram").append("TagId",tagId).append("Type","comment");
		fields = new BasicDBObject("Message",1).append("Timestamp",1);
		sort_by = new BasicDBObject("Timestamp",-1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields);
			cursor = cursor.sort(sort_by).limit(n); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				comment_set.add((String) temp.get("Message"));
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return comment_set;
	}
	
	/** 
	 * Given a product name and a tagId returns the set of likes associated with it
	 * @param product String containing the product name
	 * @param tagId String containing the tagId
	 * @return Set<String> containing the usernames of likes for the given tagId
	 */ 
	
	public static Set<String> getLikeSetForTag(String product,String tagId) { 

		Set<String> like_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product.toLowerCase().trim()); 

		query = new BasicDBObject("Channel","Instagram").append("TagId",tagId).append("Type","like");
		fields = new BasicDBObject("UsernameLike",1).append("Timestamp",1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				like_set.add((String) temp.get("UsernameLike"));
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return like_set;
	} 
	
	/** 
	 * Given a product name and a tagId returns the set of comments associated with it
	 * @param product String containing the product name
	 * @param tagId String containing the tagId
	 * @return Set<String> containing the set of comments for the given tagId
	 */ 
	
	public static Set<String> getCommentSetForTag(String product,String tagId) { 

		Set<String> comment_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product.toLowerCase().trim()); 

		query = new BasicDBObject("Channel","Instagram").append("TagId",tagId).append("Type","comment");
		fields = new BasicDBObject("Message",1).append("Timestamp",1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				comment_set.add((String) temp.get("Message"));
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return comment_set;
	} 
	
	/** 
	 * Given a product name and TagId returns a set of comments during a specified time interval 
	 * @param product String containing the product name
	 * @param tagId String containing the tagId
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Set<String> containing the Set of tags for the given products
	 */ 
	
	public static Set<String> getCommentSetForTag(String product,String tagId,Date from,Date to) { 

		Set<String> comment_set = new HashSet<String>();
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		DBObject temp;
		MongoBase mongo = null; 
		collection_name = collection_names.get(product.toLowerCase().trim()); 
		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to); 
		
		query = new BasicDBObject("Channel","Instagram").append("TagId",tagId).append("Type","comment").
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date));
		fields = new BasicDBObject("Message",1).append("Timestamp",1);

		try { 

			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query, fields); 

			while (cursor.hasNext()) { 

				temp = cursor.next();
				comment_set.add((String) temp.get("Message"));
			} 

		} catch (Exception e) {  

			e.printStackTrace(); 

		} 
		
		return comment_set;
	}  
	
	/** 
	 * Returns the Mapping between the country and its respective count
	 * @param product String containing the product name for which this has to be calculated
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return Map<String,Integer> containing the mapping between the country name and its count
	 */ 
	
	public static Map<String,Integer> getCountryBase(String product,Date from,Date to) {  
		
		double from_date = DateConverter.getJulianDate(from);
		double to_date = DateConverter.getJulianDate(to);
		Map<String,Integer> country_map = new HashMap<String,Integer>(); 
		
		DBCollection collection;
		DBCursor cursor;
		DBObject query;
		DBObject fields;
		BasicDBList query_list;
		DBObject temp;
		MongoBase mongo = null; 
		String country = "";
		int count; 
		collection_name = collection_names.get(product_name.toLowerCase().trim()); 

		query_list = new BasicDBList();
		query_list.add(new BasicDBObject("Type", "tag"));
		query_list.add(new BasicDBObject("Type", "image"));
		query_list.add(new BasicDBObject("Type", "video"));

		query = new BasicDBObject("Channel","Instagram").append("$or", query_list).
				append("Timestamp", new BasicDBObject("$gte",from_date).append("$lte", to_date)); 

		fields = new BasicDBObject("Country",1).append("CaptionText",1);

		try {  
			
			mongo = new MongoBase();
			mongo.setCollection(collection_name);
			collection = mongo.getCollection(); 
			cursor = collection.find(query,fields); 
			
			while(cursor.hasNext()) { 
				
				temp = cursor.next();
				country = (String) temp.get("Country");
				
				if(country != null && !country.isEmpty()) { 
					
					if (country_map.containsKey(country)) { 
						
						count = country_map.get(country);
						country_map.put(country,count+1); 
						
					} else { 
						
						country_map.put(country, 1);
					}
				}
			} 
			
		} catch (Exception e) { 
			
			e.printStackTrace(); 
			
		} 
		
		return country_map;
	}
	
	/** 
	 * Main function to test the functionality of the class
	 * @param args
	 */ 
	
	public static void main(String args[]) { 

		DateTime now = new DateTime();
		DateTime prev = now.minusYears(2);
		System.out.println(getCommentSetForTag("Giveaway","1038132898951639635_1975622603",prev.toDate(),now.toDate()));
		PrintUtil.printMap(getCountryMap("Giveaway",prev.toDate(),now.toDate()));
	}
}
