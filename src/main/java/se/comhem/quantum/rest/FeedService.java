package se.comhem.quantum.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.PostMapper;
import se.comhem.quantum.posts.PostsCache;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final PostsCache postsCache;
    private final PostMapper postMapper;

    @Autowired
    public FeedService(PostsCache postsCache, PostMapper postMapper) {
        this.postsCache = postsCache;
        this.postMapper = postMapper;
    }

    public FeedDto getFeed() {
        Map<Boolean, List<PostDto>> posts = postsCache.getPostsLast2Month().stream()
            .map(post -> postMapper.mapToPostDto(post))
            .collect(Collectors.partitioningBy(p -> p.getReplies() == null || p.getReplies().isEmpty()));
        return FeedDto.builder()
            .singles(posts.get(Boolean.TRUE))
            .threads(posts.get(Boolean.FALSE))
            .build();
    }
}