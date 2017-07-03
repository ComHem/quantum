package se.comhem.quantum.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Post;
import se.comhem.quantum.posts.PostsCache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final PostsCache postsCache;
    private final ModelMapper modelMapper;

    @Autowired
    public FeedService(PostsCache postsCache) {
        this.postsCache = postsCache;
        this.modelMapper = new ModelMapper();
        this.modelMapper.createTypeMap(Post.class, PostDto.class)
            .addMapping(src -> Optional.ofNullable(src.getPlatform()).map(platform -> platform.toString().toLowerCase()).orElse(null), PostDto::setPlatform);
    }

    public FeedDto getFeed() {
        Map<Boolean, List<PostDto>> posts = postsCache.getPosts().stream()
            .map(post -> modelMapper.map(post, PostDto.class))
            .collect(Collectors.partitioningBy(p -> p.getReplies() == null || p.getReplies().isEmpty()));
        return FeedDto.builder()
            .singles(posts.get(Boolean.TRUE))
            .threads(posts.get(Boolean.FALSE))
            .build();
    }
}