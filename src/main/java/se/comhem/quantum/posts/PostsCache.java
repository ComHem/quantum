package se.comhem.quantum.posts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.mongo.PostService;
import se.comhem.quantum.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PostsCache {

    private final PostService postService;

    @Autowired
    public PostsCache(PostService postService) {
        this.postService = postService;
    }

    @CacheEvict(value = "posts", allEntries = true)
    public void evictCache() {
    }

    @Cacheable("posts")
    public List<Post> getPosts() {
        List<Post> posts = postService.findAll();
        Collections.shuffle(posts, new Random(System.currentTimeMillis()));
        return posts;
    }
}
