package se.comhem.quantum.eventhub;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.FeedDto;
import se.comhem.quantum.feed.PostDto;
import se.comhem.quantum.feed.facebook.FacebookClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PostsExporter {

    private final EventHubWriteService eventHubWriteService;
    private final FacebookClient facebookClient;
    private final PostsCache postsCache;

    @Autowired
    public PostsExporter(EventHubWriteService eventHubWriteService, FacebookClient facebookClient, PostsCache postsCache) {
        this.eventHubWriteService = eventHubWriteService;
        this.facebookClient = facebookClient;
        this.postsCache = postsCache;
    }

    @Scheduled(initialDelay = 1000 * 60 * 15, fixedDelay = 1000 * 60 * 15)
    public void exportLatestPosts() {
        Stopwatch stopWatch = Stopwatch.createStarted();
        FeedDto feed = facebookClient.getLatestPosts(100);
        List<PostDto> posts = Stream.concat(feed.getSingles().stream(), feed.getThreads().stream()).collect(Collectors.toList());
        eventHubWriteService.send(posts);
        log.info("Took {} ms to export {} posts", stopWatch.elapsed().toMillis(), posts.size());

        stopWatch.reset().start();
        postsCache.reloadCache();
        postsCache.getPosts();
        log.info("Took {} ms to reload cache", stopWatch.elapsed().toMillis());
    }
}
