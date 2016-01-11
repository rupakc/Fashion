package org.kutty.features;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** 
 * Given Instagram training data randomly splits it into N mutually exclusive sets
 * @author Rupak Chakraborty
 * @for Kutty 
 * @since 23 August, 2015 
 * 
 */ 

public class SplitInstagram {

	public int NUMBER_OF_DATA_POINTS;
	public boolean [] CHECK_SAMPLED; 
	public int SIZE_OF_SPLIT;
	public int NUMBER_OF_SPLITS;
	public List<Post> post_list = new ArrayList<Post>();

	/** 
	 * public constructor to load the training data and the type of Instagram data to be loaded
	 * @param training_filename String containing the training filename
	 * @param type String containing the type of the data to be loaded i.e. (Giveaway or Sentiment)
	 */ 

	public SplitInstagram(String training_filename,String type) { 

		if (type.equalsIgnoreCase("giveaway")) {  

			FeatureUtil.populateInstagramGiveawayData(training_filename, post_list);
		}

		if (type.equalsIgnoreCase("sentiment")) { 

			FeatureUtil.populateInstagramSentimentData(training_filename, post_list);
		}

		NUMBER_OF_DATA_POINTS = post_list.size();
		CHECK_SAMPLED = new boolean[NUMBER_OF_DATA_POINTS];
	} 

	/** 
	 * Returns a pseudo random number between 0 and an upper limit
	 * @param data_points Integer representing the upper limit of the random number generator
	 * @return Integer containing the random number
	 */ 

	public int generateRandomNumber(int data_points) { 

		Random random = new Random();

		return random.nextInt(data_points);
	}

	/** 
	 * Returns an array of indices of the master dataset
	 * @param size_of_split Integer associated with the size of each data split
	 * @return an array of data points containing unique and random indices
	 */ 

	public int[] getDataPoints(int size_of_split) { 

		int [] data_points = new int[size_of_split];
		int count = 0; 
		int temp; 

		while (count < size_of_split) { 

			temp = generateRandomNumber(NUMBER_OF_DATA_POINTS);

			if (!CHECK_SAMPLED[temp]) { 

				data_points[count++] = temp;
				CHECK_SAMPLED[temp] = true;
			}
		}

		return data_points;
	}

	/** 
	 * Tags a given text with specified XML tags
	 * @param text String containing the text to be tagged
	 * @param tag_name String specifying the tag name
	 * @return String containing the tagged text
	 */ 

	public static String getXMLTaggedString(String text,String tag_name) { 

		String opening_tag = "";
		String closing_tag = "";
		String tagged_string = ""; 

		tag_name = tag_name.trim(); 

		opening_tag = "<" + tag_name + ">";
		closing_tag = "</" + tag_name + ">"; 

		tagged_string = opening_tag + "\n" + text + "\n" + closing_tag;

		return tagged_string;
	} 
	
	/** 
	 * Given a set of random posts writes them to a given file
	 * @param post_set Set<Post> containing the set of posts
	 * @param filename String containing the filename to which the data is written
	 * @param type String containing the type of post i.e. Giveaway or Sentiment
	 */ 
	
	public void writeRandomSetToFile(Set<Post> post_set,String filename,String type) { 
		
		for (Post temp : post_set) { 
			writeInstagramDataToFile(filename, temp, type);
		}
	} 
	
	/** 
	 * Given an Instagram post and filename writes it to an external file
	 * @param filename String containing the filename where it is to be written
	 * @param p Post object containing the contents of a given post
	 * @param type String containing the type of post
	 */ 
	
	public void writeInstagramDataToFile(String filename,Post p,String type) { 

		BufferedWriter bw;
		FileWriter fw;
		String tagset = "";
		String caption_text = "";
		String sentiment_label = "";
		String giveaway_label = "";
		String content_all = ""; 
		File f; 
		
		try { 
			
			f = new File(filename); 
			
			if (!f.exists()) { 

				f.createNewFile();
			} 
			
		} catch(IOException e) { 
			
			e.printStackTrace();
		} 
		
		tagset = p.getTagset();
		caption_text = p.getContent();

		if (type.equalsIgnoreCase("giveaway")) {  

			giveaway_label = p.getGiveawayLabel();
		}

		if (type.equalsIgnoreCase("sentiment")) {  

			sentiment_label = p.getSentimentLabel();
		}

		try {

			fw = new FileWriter(filename,true);
			bw = new BufferedWriter(fw);

			tagset = getXMLTaggedString(tagset, "TagSet");
			caption_text = getXMLTaggedString(caption_text,"CaptionText");

			if (type.equalsIgnoreCase("giveaway")) {  

				giveaway_label = getXMLTaggedString(giveaway_label, "ClassLabel");
			}

			if (type.equalsIgnoreCase("sentiment")) {  

				sentiment_label = getXMLTaggedString(sentiment_label, "SentimentLabel");
			}

			if (type.equalsIgnoreCase("giveaway")) {  

				content_all = tagset + "\n" + caption_text + "\n";
				content_all = content_all + giveaway_label;
				content_all = getXMLTaggedString(content_all, "Tag");
			}

			if (type.equalsIgnoreCase("sentiment")) {  

				content_all = tagset + "\n" + caption_text + "\n";
				content_all = content_all + sentiment_label;
				content_all = getXMLTaggedString(content_all, "Tag") + "\n";
			} 
			
			System.out.println(content_all);
			bw.write(content_all);
			bw.newLine();  
			bw.close(); 
			fw.close();
			
		} catch (IOException e) { 
			
			e.printStackTrace();
		} 
	}
	
	/** 
	 * Given a dataset splits it randomly into N different mutually exclusive parts
	 * @param base_file String containing the base_file name to store the data
	 * @param number_of_splits Double containing the number of data splits
	 * @param type String containing the type of data to be split i.e. Sentiment or Giveaway
	 */ 
	
	public void createSplit(String base_file,double number_of_splits,String type) { 

		SIZE_OF_SPLIT = (int) Math.floor(NUMBER_OF_DATA_POINTS/number_of_splits);
		String filename = "";  
		int data_points[]; 
		Set<Post> random_set; 

		for (int i = 1; i <= number_of_splits; i++) { 

			filename = base_file + "_" + i + ".txt";
			data_points = getDataPoints(SIZE_OF_SPLIT);
			random_set = new HashSet<Post>(); 

			for (int j = 0; j < data_points.length; j++) { 

				random_set.add(post_list.get(data_points[j]));
			}
			
			writeRandomSetToFile(random_set, filename, type);
		}
	}
	
	/** 
	 * Main function to test the functionality of the class
	 * @param args
	 */ 
	
	public static void main(String args[]) { 

		SplitInstagram split = new SplitInstagram("Giveaway.txt", "giveaway");
		split.createSplit("giveaway/split",5.0,"giveaway");
	}
}
