package org.kutty.utils;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/** 
 * Utility functions for counting POS tags in a given text using Stanford POS tagger
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 12 November,2015
 */
public class POSTagUtil {
	
	public static MaxentTagger tagger;
	
	/** 
	 * Static block to initialize the POS tag models
	 */
	static {  
		
		try {
			tagger = new MaxentTagger("taggers/wsj-0-18-left3words.tagger");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Given a text returns a tagged string representation of it
	 * @param s String containing the text to be tagged
	 * @return String with the tagged text
	 */
	public static String getTagged(String s) {

		s = tagger.tagString(s);

		return s;
	}
	
	/** 
	 * Given a piece of tagged string and a POS tag returns the count of the same 
	 * @param text String containing the tagged text
	 * @param posTag String containing the POS tag (currently supports noun,verb,adjective and determiner)
	 * @return Integer containing the count of the pos tag
	 */
	public static int getPOSCount(String text,String posTag) { 
		
		int count = 0;
		String taggedText;
		taggedText = text;
		int index = -1;
		String tag = ""; 
		String [] taggedTokens = taggedText.split(" ");
		
		for (String taggedToken : taggedTokens) { 
			
			index = taggedToken.indexOf("_"); 
			
			if (index != -1) { 
				
				tag = taggedToken.substring(index+1).trim(); 
				
				if (posTag.equalsIgnoreCase("noun")) { 
					
					if (tag.equalsIgnoreCase("NN") || tag.equalsIgnoreCase("NNP") || tag.equalsIgnoreCase("NNS") || tag.equalsIgnoreCase("NNPS")) { 
					
						count++;
					}
				}
				
				else if(posTag.equalsIgnoreCase("adjective")) { 
					
					if (tag.equalsIgnoreCase("JJ") || tag.equalsIgnoreCase("JJR") || tag.equalsIgnoreCase("JJS")) { 
						
						count++;
					}
				}
				
				else if(posTag.equalsIgnoreCase("verb")) { 
					
					if (tag.equalsIgnoreCase("VB") || tag.equalsIgnoreCase("VBD") || tag.equalsIgnoreCase("VBG") || tag.equalsIgnoreCase("VBN") || tag.equalsIgnoreCase("VBP") || tag.equalsIgnoreCase("VBZ")) { 
						
						count++;
					}
				}
				
				else if(posTag.equalsIgnoreCase("determiner")) { 
					
					if (tag.equalsIgnoreCase("DT") || tag.equalsIgnoreCase("WP") || tag.equalsIgnoreCase("WDT") || tag.equalsIgnoreCase("WP$") || tag.equalsIgnoreCase("WRB")) { 
						
						count++;
					}
				}
			}
		}
		
		return count;
	}
	
	public static void main(String args[]) { 
		
		System.out.println(getPOSCount(getTagged("Friend we rule the world"), "determiner"));
	}
}
