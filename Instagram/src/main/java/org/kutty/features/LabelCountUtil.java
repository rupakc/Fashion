package org.kutty.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * Utility class to find out label counts of classes in the dataset 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 August, 2015 
 * 
 */ 

public class LabelCountUtil {

	public static Map<String,Integer> getSpamLabelCount(List<Post> post_list) { 

		int count = 0;
		Map<String,Integer> spam_label_map = new HashMap<String,Integer>(); 
		String label = ""; 

		for(Post p : post_list) { 

			if (p.getSpamLabel() != null && !p.getSpamLabel().isEmpty()) { 

				label = p.getSpamLabel(); 

				if (spam_label_map.containsKey(label)) { 

					count = spam_label_map.get(label);
					spam_label_map.put(label, count+1); 

				} else {  

					spam_label_map.put(label, 1);
				}
			}
		}

		return spam_label_map;
	}

	public static Map<String,Integer> getSentimentLabelCount(List<Post> post_list) { 

		int count = 0;
		Map<String,Integer> sentiment_label_map = new HashMap<String,Integer>(); 
		String label = ""; 

		for(Post p : post_list) { 

			if (p.getSentimentLabel() != null && !p.getSentimentLabel().isEmpty()) { 

				label = p.getSentimentLabel(); 

				if (sentiment_label_map.containsKey(label)) { 

					count = sentiment_label_map.get(label);
					sentiment_label_map.put(label, count+1); 

				} else {  

					sentiment_label_map.put(label, 1);
				}
			}
		}

		return sentiment_label_map;
	} 

	public static Map<String,Integer> getGiveawayLabelCount(List<Post> post_list) { 

		int count = 0;
		Map<String,Integer> giveaway_label_map = new HashMap<String,Integer>(); 
		String label = ""; 

		for(Post p : post_list) { 

			if (p.getGiveawayLabel() != null && !p.getGiveawayLabel().isEmpty()) { 

				label = p.getGiveawayLabel(); 

				if (giveaway_label_map.containsKey(label)) { 

					count = giveaway_label_map.get(label);
					giveaway_label_map.put(label, count+1); 

				} else {  

					giveaway_label_map.put(label, 1);
				}
			}
		}

		return giveaway_label_map;
	} 
}
