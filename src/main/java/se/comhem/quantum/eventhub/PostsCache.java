package se.comhem.quantum.eventhub;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.PostDto;

import java.util.List;

@Service
@Slf4j
public class PostsCache {

    private final EventHubReadService eventHubReadService;

    public PostsCache(EventHubReadService eventHubReadService) {
        this.eventHubReadService = eventHubReadService;
    }

    @CacheEvict(value = "posts", allEntries = true)
    public void reloadCache() { }

    @Cacheable("posts")
    public List<PostDto> getPosts() {
        Stopwatch stopWatch = Stopwatch.createStarted();
        List<PostDto> posts = eventHubReadService.read();
        log.info("Took {} ms to fetch {} posts", stopWatch.stop().elapsed().toMillis(), posts.size());
        return posts;
    }
}
