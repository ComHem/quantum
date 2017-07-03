package se.comhem.quantum.posts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.eventhub.EventHubWriteService;
import se.comhem.quantum.integration.facebook.FacebookService;
import se.comhem.quantum.integration.twitter.TwitterService;
import se.comhem.quantum.model.Post;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Profile("!no-export")
public class PostsExporter {

    private static final int FIFTEEN_MINUTES = 1000 * 60 * 15;

    private final EventHubWriteService eventHubWriteService;
    private final FacebookService facebookService;
    private final TwitterService twitterService;
    private final PostsCache postsCache;

    @Autowired
    public PostsExporter(EventHubWriteService eventHubWriteService, FacebookService facebookService, TwitterService twitterService, PostsCache postsCache) {
        this.eventHubWriteService = eventHubWriteService;
        this.facebookService = facebookService;
        this.twitterService = twitterService;
        this.postsCache = postsCache;
    }

    @Scheduled(initialDelay = FIFTEEN_MINUTES, fixedDelay = FIFTEEN_MINUTES)
    public void exportLatestPosts() {
        List<Post> posts = facebookService.getLatestPosts(50);
        List<Post> tweets = twitterService.getTweets(50);
        List<Post> postsToExport = Stream.concat(posts.stream(), tweets.stream()).collect(Collectors.toList());
        eventHubWriteService.send(postsToExport);
        log.info("Exported {} facebook posts and {} tweets", posts.size(), tweets.size());

        postsCache.evictCache();
        List<Post> postsCached = postsCache.getPosts();
        log.info("Cache updated with {} posts/tweets", postsCached.size());
    }
}
