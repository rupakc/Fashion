package org.kutty.constants;


/**
 * Defines a set of constants which are consistently used in different packages
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 20 September, 2015
 */

public class Constants {

	public static String [] channelNames = {"Twitter","Facebook","Instagram","Youtube"};
	public static String [] brandNames = {"Forever21","FreePeople","Guess","HandM","Levis","Mango",
		"RagandBone","SevenForAllMankind","TrueReligion"};
	public static String [] MONTHS_OF_YEAR = {"january","february","march","april","may","june","july","august",
		"september","october","november","december"};
	public static String [] DAYS_OF_WEEK = {"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
	public static String [] MONTH_SHORT = {"jan","feb","mar","apr","may","jun","jul","aug","sept","oct","nov","dec"};
	public static char [] PUNCT_SET  = {' ',',',':',';','\'','\t','\n','?','-','$'};
	public static String [] EMOJI_SET = {":)",":(","^_^","-_-","<3",":D",":P",":/"};
	
	public static final int DAYS = 5;
	public static final int MEAN_DAYS = 30;
	public static final int MEDIAN_DAYS = 60;
	public static final int OLD_MIN = -10;
	public static final int OLD_MAX = 10;
	public static final int NEW_MIN = 0;
	public static final int NEW_MAX = 100;
	public static final int NORMAL_BEGIN = 0;
	public static final int NORMAL_END = 1;
	public static final int LIMIT_OF_SIG = 5;
	public static final int DIMENSION_OF_VIRAL = 11;
	public static final int MAX_MODEL_NUM = 5;
	
	public static final String SENTIMENT_TYPE = "sentiment";
	public static final String POSITIVE_LABEL = "positive";
	public static final String NEGATIVE_LABEL = "negative";
	public static final String NEUTRAL_LABEL = "neutral";
	public static final String SPAM_TYPE = "spam";
	public static final String SPAM_LABEL = "spam";
	public static final String HAM_LABEL = "ham";
	public static final String REAL_LABEL = "real";
	public static final String FAKE_LABEL = "fake";
	public static final String GIVEAWAY_TYPE = "giveaway";
	public static final String GIVEAWAY_COLLECTION = "Giveaway";
	public static final String ANALYTICS_DB = "Analytics";
	public static final String SATISFACTION_COLLECTION = "Satisfaction";
	public static final String INFLUENCE_COLLECTION = "Influence";
	public static final String FASHION_COLLECTION = "Fashion";
	public static final String USER_COLLECTION = "User";
	public static final String GIVEAWAY_FOLDER = "giveaway";
	public static final String TWITTER_FOLDER = "twitter";
	public static final String FACEBOOK_FOLDER = "facebook";
	public static final String YOUTUBE_FOLDER = "youtube";
	public static final String INSTAGRAM_FOLDER = "instagram";
	public static final String AFINN_LEXICON = "AFINN.txt";
	public static final String RESOURCES_FOLDER_NAME = "resources/";
	public static final String SENTIMENT_PRIOR_FILE = "sentiment_prior.txt";
	public static final String SPAM_PRIOR_FILE = "spam_prior.txt";
	public static final String GIVEAWAY_PRIOR_FILE = "giveaway_prior.txt";
}
