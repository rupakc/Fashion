package org.kutty.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.kutty.clean.Clean;
import org.tartarus.snowball.ext.englishStemmer;

/** 
 * Utility class for pre-processing and extracting of features 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 18 August, 2015
 * 
 */ 

public class FeatureUtil {

	public static List<String> stop_words = new ArrayList<String>();
	public static Map<String,String> spam_map = new HashMap<String,String>();
	public static Map<String,String> giveaway_map = new HashMap<String,String>();
	public static Map<String,String> sentiment_map = new HashMap<String,String>(); 
	public final static String stopword_filename = "stopwords.txt"; 
	public final static String spam_filename = "spam_label.txt";
	public final static String giveaway_filename = "giveaway_label.txt";
	public final static String sentiment_filename = "sentiment_label.txt";

	/** 
	 * Static block to load the stopwords list
	 */ 

	static { 

		loadStopList(stopword_filename);
		loadLabelMaps(giveaway_filename, giveaway_map);
		loadLabelMaps(sentiment_filename, sentiment_map);
		loadLabelMaps(spam_filename, spam_map);
	} 

	/** 
	 * Loads the stopword list in memory from a file
	 * @param filename String containing the stopwords list
	 */ 

	public static void loadStopList(String filename) { 

		BufferedReader br;
		FileReader fr; 
		String s; 

		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr); 

			while((s = br.readLine()) != null) { 

				stop_words.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	public static <T1, T2> void loadLabelMaps(String filename,Map<T1,T2> label_map) { 

		BufferedReader br;
		FileReader fr;
		String temp = ""; 
		int index; 
		T1 alias;
		T2 label; 
		
		try { 
			
			fr = new FileReader(filename);
			br = new BufferedReader(fr);

			while((temp = br.readLine()) != null) { 
				
				index = -1;
				index = temp.indexOf('=');
				
				if (index != -1) { 
					
					alias = (T1) temp.substring(0,index).trim();
					label = (T2) temp.substring(index+1).trim();
					
					label_map.put(alias, label);
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	} 

	/** 
	 * Checks whether a given word is a stopword or not
	 * @param s String whose stopword criteria has to be checked
	 * @return true if the given word is a stopword false otherwise
	 * 
	 */ 

	public static boolean isStopWord(String s) { 

		if(stop_words.contains(s)) {
			return true;
		}
		return false;
	}

	/** 
	 * Given a sentence removes all the stopwords from it
	 * @param s String containing the sentence
	 * @return String sans the stopwords
	 * 
	 */ 

	public static String removeStopWords(String s) { 

		String words[] = s.split(" ");
		String removed = ""; 

		for (int i = 0; i < words.length; i++) { 

			if (!isStopWord(words[i].toLowerCase().trim())) { 

				removed = removed + words[i].toLowerCase().trim() + " ";
			}
		}

		return removed.trim();
	}

	/** 
	 * Returns a sentence where each word is stemmed i.e reduced to its root word
	 * @param s Sentence to be stemmed
	 * @return A sentence where all the given words are stemmed
	 */ 

	public static String getStemPerWord(String s) { 

		StringTokenizer word_token;
		String stem_sentence = ""; 
		String temp = "";
		word_token = new StringTokenizer(s," "); 

		while (word_token.hasMoreTokens()) { 

			temp = word_token.nextToken();
			temp = getStem(temp);
			stem_sentence = stem_sentence + temp + " ";
		}

		return stem_sentence;
	} 

	/** 
	 * Stems a given word (i.e. Reduces it to its root word)
	 * @param s Word which is to be stemmed
	 * @return the Stemmed word
	 */ 

	public static String getStem(String s) {

		englishStemmer eng = new englishStemmer();
		eng.setCurrent(s);

		if (eng.stem()) { 

			return eng.getCurrent();
		}

		return s;
	}

	/** 
	 * Utility function to transform a given string into its NGram representation
	 * @param s String s which is to be transformed
	 * @param n Integer specifying the value of the Ngram
	 * @return String with the NGrams concatenated by a "|"
	 */ 

	public static String getNGram(String s,int n) { 

		if (n > s.length()) { 

			return "";
		}

		n = n-1;
		StringTokenizer word_token = new StringTokenizer(s," ");
		String [] word_array = new String[word_token.countTokens()];
		int count = 0;
		String n_gram_string = ""; 

		while(word_token.hasMoreTokens()) { 

			word_array[count++] = word_token.nextToken();
		} 

		for (int i = 0; i < count-n; i++) { 

			for (int j = i; j <= i+n && (i+n) < count; j++) { 

				n_gram_string = n_gram_string + word_array[j] + " ";
			}

			n_gram_string = n_gram_string + "|";
		}

		return n_gram_string;
	}

	public static String loadInstagramData(String filename) { 

		BufferedReader br;
		FileReader fr;
		String data = ""; 
		String temp = ""; 

		try { 

			fr = new FileReader(filename);
			br = new BufferedReader(fr);

			while((temp = br.readLine()) != null) { 

				data = data + temp;
			} 

			br.close();
			fr.close();

		} catch (Exception e) { 

			e.printStackTrace();
		} 

		data = Clean.removeNewLines(data);

		return data;
	}

	public static void populateInstagramData(String filename,List<Post> post_list) { 
		
		int start_tag = -1;
		int end_tag = -1;
		String content = loadInstagramData(filename);
		String caption = "";
		String tagset = ""; 
		String class_label = ""; 
		end_tag = content.indexOf("</Tag>"); 
		
		while(end_tag != -1) { 
			
			start_tag = content.indexOf("<Tag>");
			
			if (start_tag != -1) { 
				
				start_tag = content.indexOf("<TagSet>",start_tag+1);
				start_tag = content.indexOf(">",start_tag+1);
				end_tag = content.indexOf("</TagSet>", start_tag+1);
				
				tagset = content.substring(start_tag+1, end_tag).trim();
				
				System.out.println(tagset);
				
			}
		}
		
	} 

	public static void populateOtherChannelData(String filename,List<Post> post_list) { 

	} 
	
	public static void main(String args[]) { 

		System.out.println(getStem("buying"));
		System.out.println(removeStopWords("Go to the river"));
		System.out.println(getNGram("Go to the river by the sea", 2));
		//System.out.println(loadInstagramData("Giveaway.txt"));
		System.out.println(sentiment_map);
		System.out.println(spam_map);
		System.out.println(giveaway_map);
		List<Post> post_list = new ArrayList<Post>();
		populateInstagramData("Giveaway.txt", post_list);
	}
}
