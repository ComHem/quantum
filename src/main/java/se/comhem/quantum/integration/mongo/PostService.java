package se.comhem.quantum.integration.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Post;
import se.comhem.quantum.model.PostMapper;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public List<Post> getUpdatedAfter(LocalDateTime fromDate) {
        return postRepository.findByUpdateDateAfter(fromDate).stream()
                .map(postMapper::mapToPost)
                .collect(toList());
    }

    public void save(List<Post> posts) {
        List<PostDb> postsToSave = posts.stream()
                .map(postMapper::mapToPostDb)
                .collect(toList());
        postRepository.save(postsToSave);
    }

    public List<Post> findAll() {
        return postRepository.findAll().stream().map(postMapper::mapToPost).collect(toList());
    }
}
