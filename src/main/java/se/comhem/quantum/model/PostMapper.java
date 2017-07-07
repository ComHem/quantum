package se.comhem.quantum.model;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.mongo.PostDb;
import se.comhem.quantum.rest.PostDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PostMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PostMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.createTypeMap(Post.class, PostDb.class);
        this.modelMapper.createTypeMap(PostDb.class, Post.class);
        this.modelMapper.createTypeMap(Post.class, PostDto.class)
            .addMapping(src -> src.getReactions().entrySet(), PostDto::setReactions);
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

    public PostDb mapToPostDb(Post post) {
        return modelMapper.map(post, PostDb.class);
    }

    public PostDto mapToPostDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }

    public Post mapToPost(PostDb post) {
        return modelMapper.map(post, Post.class);
    }
}