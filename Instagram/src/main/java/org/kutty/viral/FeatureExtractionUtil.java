package org.kutty.viral;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.kutty.clean.Clean;
import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.dbo.Feature;
import org.kutty.dbo.ViralPost;
import org.kutty.utils.CharacterCountUtil;
import org.kutty.utils.DateConverter;
import org.kutty.utils.MatrixUtils;
import org.kutty.utils.POSTagUtil;
import org.kutty.utils.StatUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/** 
 * Utility class for extracting a set of features for post virality prediction
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 9 November,2015
 */
public class FeatureExtractionUtil {
	
	/** 
	 * Defines the pipeline for extracting the post related feature for virality prediction
	 * @param dbName String containing the database name
	 * @param collectionName String containing the collection name
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return List<ViralPost> containing the list of post specific features
	 */
	public static List<ViralPost> getPostFeaturePipeline(String dbName,String collectionName,String channel,Date from,Date to) { 
		 
		List<ViralPost> viralPost = new ArrayList<ViralPost>();
		List<BasicDBObject> postList = getPostForChannel(dbName, collectionName, channel, from, to);
		ViralPost viral;  
		String taggedText = ""; 
		
		for (BasicDBObject post : postList) { 
			
			viral = new ViralPost();
			viral.setChannel(channel);
			
			if (!channel.equalsIgnoreCase("Instagram")) { 
				
				viral.setContent(post.getString("Message"));
				
			} else { 
				
				viral.setContent(post.getString("CaptionText"));
			}
			
			if (channel.equalsIgnoreCase("Instagram")) { 
				
				viral.setHourOfDay(DateConverter.getHourOfDay(post.getDouble("Timestamp")));
			}
			
			else if (channel.equalsIgnoreCase("Reddit")) {  
				
				viral.setHourOfDay(DateConverter.getHourOfDay(post.getDouble("TimeStamp")));
			}
			
			else { 
				
				viral.setHourOfDay(DateConverter.getHourOfDay(post.getDate("TimeStamp")));
			}
			
			if (viral.getContent() != null) {  
				
				viral.setHashTags(CharacterCountUtil.getCharacterCount(viral.getContent(), '#'));
				viral.setPunctCount(CharacterCountUtil.getPunctuationCount(viral.getContent()));
				viral.setPositiveWordCount(CharacterCountUtil.getCountPositiveWords(viral.getContent()));
				viral.setNegativeWordCount(CharacterCountUtil.getCountNegativeWords(viral.getContent()));
				viral.setEmoticonCount(CharacterCountUtil.getEmoticonCount(viral.getContent()));
				
				taggedText = POSTagUtil.getTagged(Clean.cleanHTML(Clean.removePunctuationAndJunk(viral.getContent())));
				
				viral.setAdjectiveCount(POSTagUtil.getPOSCount(taggedText, "adjective"));
				viral.setNounCount(POSTagUtil.getPOSCount(taggedText, "noun"));
				viral.setVerbCount(POSTagUtil.getPOSCount(taggedText, "verb"));
				viral.setDeterminantCount(POSTagUtil.getPOSCount(taggedText, "determiner"));
				
				viral.setSpreadCount(spreadCountGenerator(post, channel)); 
				
				viralPost.add(viral);
			}
		} 
		
		return viralPost;
	}
	
	/** 
	 * Retrieves a set of post objects from the database for a given channel
	 * @param dbName String containing the database name
	 * @param collectionName String containing the collection name
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return List<BasicDBObject> containing the list of post objects
	 */
	public static List<BasicDBObject> getPostForChannel(String dbName,String collectionName,String channel,Date from,Date to) { 
		
		List<BasicDBObject> postList = new ArrayList<BasicDBObject>();
		MongoBase mongo;
		BasicDBObject query;
		DBCollection collection;
		DBCursor cursor; 
		
		query = queryGenerator(channel, from, to); 
		
		try { 
			
			mongo = new MongoBase();
			mongo.setDB(dbName);
			mongo.setCollection(collectionName); 
			
			collection = mongo.getCollection();
			cursor = collection.find(query);
			
			while(cursor.hasNext()) { 
				
				if (!channel.equalsIgnoreCase("Instagram")) {  
					
					postList.add((BasicDBObject) cursor.next()); 
					
				} else { 
					
					postList.add((BasicDBObject) cursor.next());
				}
			} 
			
		} catch (UnknownHostException e) { 
			
			e.printStackTrace();
		} 
		
		return postList;
	}
	
	/** 
	 * Generates queries for retrieving the posts for a given channel
	 * @param channel String containing the channel name
	 * @param from Date containing the starting date
	 * @param to Date containing the ending date
	 * @return BasicDBObject containing the query
	 */
	public static BasicDBObject queryGenerator(String channel,Date from,Date to) { 
		
		BasicDBObject query = null;
		BasicDBList queryList; 
		
		if (channel.equalsIgnoreCase("Reddit")) {  
			
			double fromDate = DateConverter.getJulianDate(from);
			double toDate = DateConverter.getJulianDate(to); 
			
			query = new BasicDBObject("Channel",channel).
					append("TimeStamp", new BasicDBObject("$gte",fromDate).append("$lte", toDate));
		}
		
		else if (channel.equalsIgnoreCase("Instagram")) { 
			
			double fromDate = DateConverter.getJulianDate(from);
			double toDate = DateConverter.getJulianDate(to); 
			queryList = new BasicDBList(); 
			
			queryList.add(new BasicDBObject("Type","image"));
			queryList.add(new BasicDBObject("Type","tag"));
			queryList.add(new BasicDBObject("Type","video"));
			
			query = new BasicDBObject("Channel",channel).append("$or",queryList).
					append("Timestamp", new BasicDBObject("$gte",fromDate).append("$lte",toDate));
		}
		
		else { 
			
			query = new BasicDBObject("Channel",channel).
					append("TimeStamp", new BasicDBObject("$gte",from).append("$lte", to));
		}
		
		return query;
	}
	
	/** 
	 * Returns the spread count for a given channel
	 * @param post BasicDBObject containing the post object
	 * @param channel String containing the channel name
	 * @return Long containing the spread count value
	 */
	@SuppressWarnings("unused")
	public static Long spreadCountGenerator(BasicDBObject post,String channel) {  
		
		Long spreadCount = 0L; 
		Object k; 
		
		if (channel.equalsIgnoreCase("Instagram")) { 
			
			spreadCount = post.getLong("LikeCount") + post.getLong("CommentCount");
		}
		
		else if(channel.equalsIgnoreCase("Reddit")) { 
			
			spreadCount = post.getLong("Score") + post.getLong("CommentCount");
		}
		
		else if(channel.equalsIgnoreCase("Youtube")) { 
			
			spreadCount = (long) (post.getInt("ReplyCount") + post.getInt("LikeCount"));
		}
		
		else if(channel.equalsIgnoreCase("Facebook")) { 
			
			if ((k = post.get("Likes")) != null) { 
				
				spreadCount = spreadCount + (long) post.getInt("Likes");
			}
			
			if ((k = post.get("Shares")) != null) { 
				
				spreadCount = spreadCount + (long) post.getInt("Shares");
			}
			
			if ((k = post.get("Comments")) != null) { 
				
				spreadCount = spreadCount + (long) post.getInt("Comments");
			}
		}
		
		else if(channel.equalsIgnoreCase("Twitter")) { 
			
			spreadCount = (long) post.getInt("RetweetCount");
		}
		
		return spreadCount;
	}
	
	/** 
	 * Adaptor function to convert a Viral Post object into a Feature object
	 * @param viralPosts List<ViralPost> containing the viral post objects
	 * @return List<Feature> containing the feature vector objects
	 */
	public static List<Feature> getFeatureAdaptor(List<ViralPost> viralPosts) { 
		
		Double featureVector[];
		Feature feature;
		List<Feature> featureList = new ArrayList<Feature>();
		
		for (ViralPost viral : viralPosts) { 
			
			feature = new Feature();
			feature.setDimension(Constants.DIMENSION_OF_VIRAL);
			featureVector = new Double[Constants.DIMENSION_OF_VIRAL];
			
			featureVector[0] = (double)(long)viral.getSpreadCount();
			featureVector[1] = (double) viral.getHourOfDay();
			featureVector[2] = (double) viral.getHashTags();;
			featureVector[3] = (double) viral.getPunctCount();
			featureVector[4] = (double) viral.getNounCount();
			featureVector[5] = (double) viral.getVerbCount();
			featureVector[6] = (double) viral.getDeterminantCount();
			featureVector[7] = (double) viral.getAdjectiveCount();
			featureVector[8] = (double) viral.getPositiveWordCount();
			featureVector[9] = (double) viral.getNegativeWordCount();
			featureVector[10] = (double) viral.getEmoticonCount();
			
			feature.setFeatureVector(featureVector);
			
			if (viral.isViral()) {  
				
				feature.setClassLabel(1); 
				
			} else { 
				
				feature.setClassLabel(0);
			}
			
			featureList.add(feature);
		}
		
		return featureList;
	} 
	
	/** 
	 * Standardizes the feature set by subtracting mean and dividing by standard deviation
	 * @param featureList List<Feature> to be standardized
	 * @return List<Feature> containing the feature list
	 */
	public static List<Feature> standardizeFeatureSet(List<Feature> featureList) { 
		
		List<Double> featureSet = new ArrayList<Double>(); 
		Double [] featureArray = new Double[featureList.get(0).getFeatureVector().length];
		Double [] meanArray;
		Double [] stdArray; 
		Double trainSet[][] = new Double[featureList.size()][featureList.get(0).getFeatureVector().length];
		Feature tempFeature; 
		
		for(int i = 0; i < featureList.size(); i++) { 
			
			featureArray = featureList.get(i).getFeatureVector();
			
			for (int j = 0; j < featureArray.length; j++) { 
				
				trainSet[i][j] = featureArray[j];
			}
		}
	
		meanArray = MatrixUtils.getMeanVector(trainSet);
		stdArray = MatrixUtils.getStdVector(trainSet);
	
		for (int i = 0; i < trainSet[i].length; i++) { 
			
			featureSet.clear(); 
			
			for (int j = 0; j < trainSet.length; j++) { 
				
				featureSet.add(trainSet[j][i]);
			}
			
			StatUtils.standardizeData(featureSet, meanArray[i], stdArray[i]);
			
			for (int j = 0; j < featureSet.size(); j++) { 
				
				trainSet[j][i] = featureSet.get(j);
			}
		} 
		
		for (int i = 0; i < featureList.size(); i++) { 
			
			for (int j = 0; i < trainSet[i].length; i++) { 
				
				featureArray[j] = trainSet[i][j];
			
			} 
			
			tempFeature = featureList.get(i);
			tempFeature.setFeatureVector(featureArray);
			featureList.set(i, tempFeature);
		}
		
		return featureList;
	}
	
	public static void main(String args[]) throws UnknownHostException { 
		
		DateTime to = new DateTime();
		DateTime from = to.minusDays(17); 
		List<ViralPost> featureList = getPostFeaturePipeline("Central", "Forever21", "Twitter", from.toDate(), to.toDate());
		List<Feature> features = getFeatureAdaptor(featureList);
		features = standardizeFeatureSet(features);
		System.out.println(featureList.size());
	}
}
