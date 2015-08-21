package org.kutty.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 
 * Class for extracting the NGram features from an external file and writing it to an external file
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 19 August, 2015
 * 
 */ 

public class NGram {
	
	public static void NGramExtractionPipeline(String filename,int n) { 
		
		List<Post> post_list = new ArrayList<Post>();
		FeatureUtil feat = new FeatureUtil(); 
		FeatureUtil.populateInstagramGiveawayData(filename, post_list);
		Map<String,Integer> label_count_map;
		label_count_map = feat.giveaway_count_map; 
		
		for (Post p : post_list) { 
			
			String content = p.getContent();
			content = content.toLowerCase().trim();
			content = FeatureUtil.cleanString(content);
			content = FeatureUtil.removeStopWords(content);
			content = FeatureUtil.getStemPerWord(content);
			content = FeatureUtil.getNGram(content, n);
			p.setContent(content);
		}
		
		label_count_map.putAll(LabelCountUtil.getGiveawayLabelCount(post_list));
		
	}
	
	public static void main(String args[]) { 
		NGramExtractionPipeline("insta_test.txt", 3);
	}
}
