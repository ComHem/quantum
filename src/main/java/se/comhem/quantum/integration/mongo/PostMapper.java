package se.comhem.quantum.integration.mongo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Post;

@Service
public class PostMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PostMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.createTypeMap(Post.class, PostDb.class);
        this.modelMapper.createTypeMap(PostDb.class, Post.class);
    }

    public PostDb mapToPostDb(Post post) {
        return modelMapper.map(post, PostDb.class);
    }

    public Post mapToPost(PostDb post) {
        return modelMapper.map(post, Post.class);
    }
}