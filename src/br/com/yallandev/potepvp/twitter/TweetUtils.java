package br.com.yallandev.potepvp.twitter;

import br.com.yallandev.potepvp.BukkitMain.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetUtils {
	
	private static Twitter twitterBans = null;
	private static Twitter twitterOficial = null;
	
	public static boolean isUsing() {
		return false;
	}

	public static boolean tweetBans(String tweet) {
		Twitter twitter = null;

		if (TweetUtils.twitterBans == null) {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey(Configuration.BANS_CONSUMER_KEY.getMessage())
					.setOAuthConsumerSecret(Configuration.BANS_CONSUMER_SECRET.getMessage()).setOAuthAccessToken(Configuration.BANS_ACESS_TOKEN.getMessage())
					.setOAuthAccessTokenSecret(Configuration.BANS_ACESS_SECRET.getMessage());
			TweetUtils.twitterBans = new TwitterFactory(cb.build()).getInstance();
		}
		
		twitter = TweetUtils.twitterBans;

		try {
			twitter.updateStatus(tweet);
			return true;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean tweetOficial(String tweet) {
		if (!isUsing())
			return false;
		
		Twitter twitter = null;

		if (TweetUtils.twitterOficial == null) {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey(Configuration.OFICIAL_CONSUMER_KEY.getMessage())
					.setOAuthConsumerSecret(Configuration.OFICIAL_CONSUMER_SECRET.getMessage()).setOAuthAccessToken(Configuration.OFICIAL_ACESS_TOKEN.getMessage())
					.setOAuthAccessTokenSecret(Configuration.OFICIAL_ACESS_SECRET.getMessage());
			TweetUtils.twitterOficial = new TwitterFactory(cb.build()).getInstance();
		}
		
		twitter = TweetUtils.twitterOficial;

		try {
			twitter.updateStatus(tweet);
			return true;
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return false;
	}

}
