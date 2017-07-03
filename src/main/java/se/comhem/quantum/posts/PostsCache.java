package se.comhem.quantum.posts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.eventhub.EventHubReadService;
import se.comhem.quantum.model.Post;

import java.util.List;

@Service
@Slf4j
public class PostsCache {

    private final EventHubReadService eventHubReadService;

    @Autowired
    public PostsCache(EventHubReadService eventHubReadService) {
        this.eventHubReadService = eventHubReadService;
    }

    @CacheEvict(value = "posts", allEntries = true)
    public void evictCache() { }

    @Cacheable("posts")
    public List<Post> getPosts() {
        return eventHubReadService.read();
    }
}
