package se.comhem.quantum.posts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.eventhub.EventHubWriteService;
import se.comhem.quantum.integration.facebook.FacebookService;
import se.comhem.quantum.integration.mongo.PostService;
import se.comhem.quantum.integration.twitter.TwitterService;
import se.comhem.quantum.model.Post;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@Profile("!no-export")
public class PostsExporter {

    private static final int FIFTEEN_MINUTES = 1000 * 60 * 15;

    private final EventHubWriteService eventHubWriteService;
    private final FacebookService facebookService;
    private final TwitterService twitterService;
    private final PostsCache postsCache;
    private final PostService postService;

    @Autowired
    public PostsExporter(EventHubWriteService eventHubWriteService, FacebookService facebookService, TwitterService twitterService, PostsCache postsCache, PostService postService) {
        this.eventHubWriteService = eventHubWriteService;
        this.facebookService = facebookService;
        this.twitterService = twitterService;
        this.postsCache = postsCache;
        this.postService = postService;
    }

    @PostConstruct
    private void postConstruct() {
        log.info("Loading initial posts...");
        List<Post> posts = postsCache.getPosts();
        if (posts.isEmpty()) {
            exportLatestPosts();
        }
    }

    @Scheduled(initialDelay = 1000, fixedDelay = FIFTEEN_MINUTES)
    public void exportLatestPosts() {
        log.info("Export latest posts...");
        List<Post> facebookPosts = facebookService.getLatestPosts(50);
        List<Post> tweets = twitterService.getTweets(100);
        log.info("Found {} facebook posts and {} tweets", facebookPosts.size(), tweets.size());
        Map<String, Post> postsCached = postsCache.getPosts().stream().collect(Collectors.toMap(Post::getKey, p -> p));

        List<Post> postsToExport = Stream.concat(filterExport(facebookPosts, postsCached), filterExport(tweets, postsCached))
            .sorted(Comparator.comparing(Post::getUpdateDate))
            .collect(toList());
        eventHubWriteService.send(postsToExport);
        postService.save(postsToExport);
        log.info("Exported {} posts", postsToExport.size());

        postsCache.evictCache();
        List<Post> newPostsCached = postsCache.getPosts();
        log.info("Cache updated with {} posts/tweets", newPostsCached.size());
    }

    private Stream<Post> filterExport(List<Post> newPosts, Map<String, Post> postsCached) {
        return newPosts.stream().filter(newPost -> PostFilter.newOrUpdatedPost(newPost, postsCached));
    }
}
