package org.kutty.toptrends;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.kutty.dbo.Topic;
import org.kutty.utils.PrintUtil;

/** 
 * Implements LDA for topic detection using condensed Gibbs Sampling
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 12 January,2016
 */

public class LDA {

	public int topicNum;

	/** 
	 * public constructor to 
	 * @param topicNum
	 */
	public LDA(int topicNum) {  

		this.topicNum = topicNum;
	}

	/**
	 * Preprocesses the string for carrying out LDA involves stopword removal etc 
	 * @param s String which is to be preprocessed
	 * @return String array containing the words in the documents
	 */
	public static String[] preprocessingPipeline(String s) { 

		s = LDAUtil.removePunctuation(s);
		s = LDAUtil.removeStopWords(s); 

		return LDAUtil.wordTokenize(s);
	}

	/** 
	 * Calculates the topic with the maximum likelihood for a given word
	 * @param word String containing the word
	 * @param topicArray Topic Array containing the list of topics
	 * @return Integer containing the topic number of the given word
	 */
	public int getNewTopicNumber(String word,Topic[] topicArray) { 

		int tempTopicNum = 0;
		double tempProbability = Double.MIN_VALUE;
		double probability; 

		for (int i = 0; i < topicArray.length; i++) { 

			probability = topicArray[i].topicProbability; 

			if (topicArray[i].word_probabilities.containsKey(word)) {  

				probability = probability * topicArray[i].word_probabilities.get(word); 

			} else { 

				probability = probability*0.0001;
			}

			if (probability > tempProbability) { 

				tempProbability = probability;
				tempTopicNum = i;
			}
		}

		return tempTopicNum;
	}
	
	/** 
	 * Returns the list of topics for a given text
	 * @param s String containing the given text
	 * @return Topic[] array containing the list of topic objects
	 */
	public Topic[] getTopicList(String s) { 

		String words[] = preprocessingPipeline(s);
		Topic topicList [] = new Topic[this.topicNum]; 
		int count = 0; 
		int [] initialDistribution = LDAUtil.generateInitialDistribution(words.length, this.topicNum);
		LDAUtil.initTopicObjects(topicList);

		// Creates the initial distribution of the topics and words 

		for (int i = 0; i < initialDistribution.length; i++) { 

			topicList[initialDistribution[i]].topicWords.add(words[i]);
		} 

		while(count <= 1000) { 

			// Calculate the topic and word distributions 

			for (int i = 0; i < topicList.length; i++) {  

				topicList[i].topicProbability = (topicList[i].topicWords.size()*1.0)/(words.length + 1.0); 
				
				for (String word : topicList[i].topicWords) {  

					double word_count_probability = (LDAUtil.getWordCount(topicList[i], word))/(topicList[i].topicWords.size()+1); 

					if(!topicList[i].word_probabilities.containsKey(word)) {  

						topicList[i].word_probabilities.put(word, word_count_probability);
					} 
				}
			}

			//Reassign the words to the most likely topic

			for (int i = 0; i < topicList.length; i++) { 
				
				Iterator<String> iter = topicList[i].topicWords.iterator();
				while (iter.hasNext()) {  

					String word = iter.next();  

					int newTopicNum = getNewTopicNumber(word, topicList); 

					if (newTopicNum != i) { 

						topicList[newTopicNum].topicWords.add(word); 

						if (topicList[i].topicWords.contains(word)) {   
							
							iter.remove();
							topicList[i].word_probabilities.remove(word);
						}
					}
				}
			}
			
			count++;
		}

		for (int i = 0; i < topicList.length; i++) { 

			topicList[i].setTopicName();
		}

		return topicList;
	}

	/**
	 * Main function to test the functionality of the module
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException{ 

		FileReader fr;
		BufferedReader br;
		String s;
		String text = "";

		fr = new FileReader("ldatest.txt");
		br = new BufferedReader(fr); 

		while((s = br.readLine()) != null) {  
			
			if (!s.isEmpty()) {
				
				text = text + s;
			}
		} 

		br.close();
		fr.close();

		LDA lda = new LDA(25);

		for(Topic t :lda.getTopicList(text)) { 

			System.out.println("----------------------------------------\n");
			System.out.println(t.topicWords.size());
			PrintUtil.printMap(t.word_probabilities);
			//System.out.println(t.word_probabilities);
			System.out.println(t.topicName);
			System.out.println("-----------------------------------------\n");
		}
	}
}
