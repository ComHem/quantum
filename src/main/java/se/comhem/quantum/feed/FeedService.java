package se.comhem.quantum.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.facebook.FacebookClient;
import se.comhem.quantum.feed.twitter.TwitterService;

import java.util.Collections;
import java.util.List;

@Service
public class FeedService {

    private final TwitterService twitterService;
    private final FacebookClient facebookClient;

    @Autowired
    public FeedService(TwitterService twitterService, FacebookClient facebookClient) {
        this.twitterService = twitterService;
        this.facebookClient = facebookClient;
    }

    public FeedDto getFeed() {
        FeedDto facebookFeed = facebookClient.getLatestPosts(20);
        FeedDto twitterFeeds = twitterService.getTweets();
        return mergeFeed(facebookFeed, twitterFeeds);
    }

    private FeedDto mergeFeed(FeedDto facebookFeed, FeedDto twitterFeeds) {
        FeedDto f = new FeedDto();
        List<PostDto> mergedSingles = facebookFeed.getSingles();
        List<PostDto> mergedThreads = facebookFeed.getThreads();
        twitterFeeds.getSingles().forEach(mergedSingles::add);
        twitterFeeds.getThreads().forEach(mergedThreads::add);
        Collections.shuffle(mergedSingles);
        Collections.shuffle(mergedThreads);
        return FeedDto.builder()
                .singles(mergedSingles)
                .threads(mergedThreads).build();
    }
}