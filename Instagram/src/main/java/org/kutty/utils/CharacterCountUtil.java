package org.kutty.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.kutty.constants.Constants;

/** 
 * Defines a set of character counting utility functions
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 11 November,2015
 */
public class CharacterCountUtil {
	
	/** 
	 * Calculates the count of a particular character in a given string
	 * @param s String containing the sentence
	 * @param ch Character which is to be counted
	 * @return Integer containing the count of the character
	 */
	public static int getCharacterCount(String s,char ch) {  
		
		int count = 0;
		for (int i = 0; i < s.length(); i++) {  
			if (s.charAt(i) == ch) { 
				count++;
			}
		} 
		
		return count;
	}
	
	/** 
	 * Calculates the count of punctuations and whitespaces
	 * @param s String containing the sentence
	 * @return Integer containing the count of punctuations
	 */
	public static int getPunctuationCount(String s) { 
		
		int count = 0;
		Map<Character,Character> characterMap = new HashMap<>(); 
		
		for (int i = 0; i < Constants.PUNCT_SET.length; i++) { 
			
			characterMap.put(Constants.PUNCT_SET[i], Constants.PUNCT_SET[i]);
		}
		
		for (int i = 0; i < s.length(); i++) { 
			
			if (characterMap.containsKey(s.charAt(i))) { 
				count++;
			}
		}
		
		return count;
	}
	
	/** 
	 * Calculates the count of emoticons in a given string
	 * @param s String containing the sentence
	 * @return Integer containing the emoticon count
	 */
	public static int getEmoticonCount(String s) { 
		
		int count = 0;
		Map<String,String> emojiMap = new HashMap<String,String>();
		s = s.toLowerCase(); 
		
		for (int i = 0; i < Constants.EMOJI_SET.length; i++) { 
			
			emojiMap.put(Constants.EMOJI_SET[i], Constants.EMOJI_SET[i]);
		}
		
		for (String temp : s.split(" ")) { 
			
			if (emojiMap.containsKey(temp)) { 
				
				count++;
			}
		}
		
		return count;
	}
	
	/** 
	 * Counts the number of positive words present in a given post
	 * @param s String containing the post
	 * @return Integer containing the count of positive words
	 */
	public static int getCountPositiveWords(String s) {  
		
		int count = 0;
		s = s.toLowerCase();
		Map<String,Integer> lexicon = new HashMap<String,Integer>();
		countWordUtil(Constants.AFINN_LEXICON,lexicon);
		
		for (String temp : s.split(" ")) { 
			
			if (lexicon.containsKey(temp)) { 
				
				if (lexicon.get(temp) > 0) {
					
					count++;
				}
			}
		}
		
		return count;
	}
	
	/** 
	 * Counts the number of positive words present in a given post
	 * @param s String containing the post
	 * @return Integer containing the count of positive words
	 */
	public static int getCountNegativeWords(String s) { 
		
		int count = 0;
		s = s.toLowerCase();
		Map<String,Integer> lexicon = new HashMap<String,Integer>();
		countWordUtil(Constants.AFINN_LEXICON,lexicon);
		
		for (String temp : s.split(" ")) { 
			
			if (lexicon.containsKey(temp)) { 
				
				if (lexicon.get(temp) < 0) {
					
					count++;
				}
			}
		}
		
		return count;
	}
	
	/** 
	 * Utility function to initialize the lexicon
	 * @param filename String containing the filename of the lexicon
	 * @param lexicon Map<String,Integer> containing the word and its weight
	 */
	public static void countWordUtil(String filename,Map<String,Integer> lexicon) { 
		
		FileReader fr;
		BufferedReader br;
		String temp; 
		int index = -1; 
		
		try { 
			
			fr = new FileReader(filename);
			br = new BufferedReader(fr); 
			
			while((temp = br.readLine()) != null) { 
				
				index = temp.indexOf("\t"); 
				
				if (index != -1) {  
					
					lexicon.put(temp.substring(0, index).trim(), Integer.valueOf(temp.substring(index+1).trim()));
				}
			} 
			
		} catch (IOException e) { 
			
			e.printStackTrace();
		}
	} 
}
