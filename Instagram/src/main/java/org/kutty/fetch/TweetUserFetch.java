package org.kutty.fetch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.kutty.constants.Constants;
import org.kutty.db.MongoBase;
import org.kutty.dbo.TweetUser;

import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;



/** 
 * Retrieves user info from twitter given a username or userId 
 * 
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 3 October,2015
 */

public class TweetUserFetch {

	Twitter twitter = new TwitterFactory().getInstance(); 
	AccessToken accessToken = null;  
	String collectionName = Constants.USER_COLLECTION;
	
	/** 
	 * 
	 * @param collectionName
	 * @throws IOException
	 */ 
	
	public TweetUserFetch(String collectionName) throws IOException { 

		setKeys(); 
		this.collectionName = collectionName.toLowerCase();
	}
	
	/** 
	 * 
	 */ 
	
	public TweetUserFetch() { 

		setKeys();
	}
	
	/** 
	 * 
	 * @param user
	 * @param tweetUser
	 */ 
	
	public void userFetchPipeline(User user,TweetUser tweetUser) {

		try { 

			MongoBase mongo = null; 

			try { 
				mongo = new MongoBase();
				mongo.setCollection(collectionName);
				getUserInfo(user, tweetUser);
				mongo.putInDB(tweetUser); 

			} catch (UnknownHostException e) {
				e.printStackTrace(); 
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	} 

	/** 
	 * Sets the access token for OAuth with user given values
	 * 
	 * @param access_token Access token which is to be set
	 * @param access_token_secret Access token secret which is to be set
	 * @param consumer_key String containing the consumer key which is to be set
	 * @param consumer_key_secret String containing the consumer key secret which is to be set
	 */ 

	public void setKeys(String access_token,String access_token_secret,String consumer_key,String consumer_key_secret) {

		accessToken = new AccessToken(access_token, access_token_secret);
		twitter.setOAuthConsumer(consumer_key,consumer_key_secret);
		twitter.setOAuthAccessToken(accessToken);	
	} 


	/** 
	 * Sets the default value of keys for Twitter API access 
	 */ 

	public void setKeys() { 

		accessToken = new AccessToken("450529986-MRceSC7o2s5Ql6UtRjrD2QjKvkAMXrV5I1bbampV", "zLkRczDjc2jVPUj2KJa7euT37Ge9WjJgOK6ZcaidyKYsT");
		twitter.setOAuthConsumer("h5ly20TCX2cC0Pj8Ul269NFdX", "SoXEB7wlPZruHfL2aPnLbjEQz0TKaySqV3eLlKlp2AEaEpvvZ8");
		twitter.setOAuthAccessToken(accessToken);
	}

	/**
	 * Sets the Access Token, Access Token Secret, Consumer-key and Consumer-key secret with values taken from a file
	 * The file format is as follows:
	 * Access-Token=Your_access_token
	 * Access-Token_Secret=Your_access_token_secret
	 * Consumer-key=Your_consumer_key
	 * Consumer-key-secret=Your_consumer_key-secret 
	 * 
	 * @param filename String containing the  file from which key is to be read
	 * @throws IOException
	 */

	public void setKeys(String filename)throws IOException { 

		BufferedReader br;
		FileReader fr;
		String [] arr = new String[4];
		String s = ""; 
		int index;
		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		for (int i = 0; i < arr.length; i++) { 

			if ((s = br.readLine()) != null) { 

				index = s.indexOf("=");
				arr[i] = s.substring(index+1).trim();

			}
		}

		br.close();
		fr.close();

		accessToken = new AccessToken(arr[0], arr[1]);
		twitter.setOAuthConsumer(arr[2],arr[3]);
		twitter.setOAuthAccessToken(accessToken);
	}

	/** 
	 * 
	 * @param user
	 * @param tweetUser
	 * @throws TwitterException
	 */ 

	public void getUserInfo(User user,TweetUser tweetUser) throws TwitterException { 

		tweetUser.setActiveSince(user.getCreatedAt());
		tweetUser.setDescription(user.getDescription());
		tweetUser.setFavoriteCount((long) user.getFavouritesCount());
		tweetUser.setFollowerCount((long) user.getFollowersCount());
		tweetUser.setFriendCount((long) user.getFriendsCount());
		tweetUser.setStatusCount((long) user.getStatusesCount());
		tweetUser.setUrl(user.getURL());
		tweetUser.setUserId(user.getId());
		tweetUser.setUserLang(user.getLang());
		tweetUser.setUserScreenName(user.getScreenName());
		tweetUser.setFollowerList(getFollowerList(user));
		tweetUser.setFriendList(getFriendList(user));

	}

	/** 
	 * 
	 * @param user
	 * @return
	 * @throws TwitterException
	 */ 

	public List<String> getFollowerList(User user) throws TwitterException { 

		PagableResponseList<User> followers = twitter.getFollowersList(user.getScreenName(), -1);
		List<String> followerList = new ArrayList<String>(); 
		int limit = 1;
		while(followers.getNextCursor() != 0 && limit != 1) { 

			addToUserList(followers, followerList);
			followers = twitter.getFollowersList(user.getScreenName(),followers.getNextCursor());
		}

		addToUserList(followers, followerList);

		return followerList;
	}

	/** 
	 * 
	 * @param user
	 * @return
	 * @throws TwitterException
	 */ 

	public List<String> getFriendList(User user) throws TwitterException { 

		PagableResponseList<User> friends = twitter.getFriendsList(user.getScreenName(), -1);
		List<String> friendList = new ArrayList<String>(); 
		int limit = 1;
		while(friends.getNextCursor() != 0 && limit != 1) { 

			addToUserList(friends, friendList);
			friends = twitter.getFollowersList(user.getScreenName(),friends.getNextCursor());
		}

		addToUserList(friends, friendList);

		return friendList;
	}

	/** 
	 * 
	 * @param responseList
	 * @param followerList
	 */ 

	public void addToUserList(PagableResponseList<User>responseList,List<String>followerList) {

		for (User tempUser : responseList) { 

			followerList.add(tempUser.getScreenName());
		}
	}

	/** 
	 * 
	 * @param query
	 * @return
	 */ 

	public ResponseList<User> searchUser(String query) {  

		ResponseList<User> pagableResults = null; 

		try { 

			pagableResults = twitter.searchUsers(query,0);
			ResponseList<User> tempResults; 

			for (int i = 1; i <= 50; i++) { 

				tempResults = twitter.searchUsers(query,i);
				pagableResults.addAll(tempResults);
			} 

		} catch (TwitterException e) { 

			e.printStackTrace();
		}

		return pagableResults;
	}

	/** 
	 * 
	 * @param userScreenName
	 * @return
	 * @throws TwitterException
	 */

	public User getUserData(String userScreenName) throws TwitterException { 

		return twitter.showUser(userScreenName);
	}
}
