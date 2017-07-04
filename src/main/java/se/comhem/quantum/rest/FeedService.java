package se.comhem.quantum.rest;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Platform;
import se.comhem.quantum.model.Post;
import se.comhem.quantum.posts.PostsCache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        this.modelMapper.createTypeMap(Post.class, PostDto.class);
        this.modelMapper.addConverter(new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime localDateTime) {
                return Optional.ofNullable(localDateTime)
                    .map(date -> date.format(DateTimeFormatter.ISO_DATE_TIME))
                    .orElse(null);
            }
        });
        this.modelMapper.addConverter(new AbstractConverter<Platform, String>() {
            @Override
            protected String convert(Platform platform) {
                return Optional.ofNullable(platform)
                    .map(date -> platform.toString().toLowerCase())
                    .orElse(null);
            }
        });
    }

    public FeedDto getFeed() {
        Map<Boolean, List<PostDto>> posts = postsCache.getPosts().stream()
            .filter(post -> post.getUpdateDate().isAfter(LocalDateTime.now().minusMonths(2)))
            .map(post -> modelMapper.map(post, PostDto.class))
            .collect(Collectors.partitioningBy(p -> p.getReplies() == null || p.getReplies().isEmpty()));
        return FeedDto.builder()
            .singles(posts.get(Boolean.TRUE))
            .threads(posts.get(Boolean.FALSE))
            .build();
    }
}