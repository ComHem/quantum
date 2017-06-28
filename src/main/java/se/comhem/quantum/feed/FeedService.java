package se.comhem.quantum.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.twitter.TwitterService;
import twitter4j.TwitterException;

@Service
public class FeedService {

    private final TwitterService twitterService;

    @Autowired
    public FeedService(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    public FeedDto getFeed() throws TwitterException {
        return twitterService.getTweets();
    }
}