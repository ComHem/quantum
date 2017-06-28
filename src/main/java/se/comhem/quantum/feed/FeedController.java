package se.comhem.quantum.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.comhem.quantum.feed.twitter.TwitterService;
import twitter4j.TwitterException;

@RestController
public class FeedController {
    @Autowired
    private TwitterService twitterService;
    @Autowired
    private FeedService feedService;

    @RequestMapping(value = "/api/feed", method = RequestMethod.GET)
    public FeedDto findFeed() throws TwitterException {
        return feedService.getFeed();
    }
}
