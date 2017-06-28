package se.comhem.quantum.facebook;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Ordering;
import facebook4j.Reading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FacebookClient {

    private final Facebook facebook;
    private final String page;
    private final String fields;

    @Autowired
    public FacebookClient(Facebook facebook,
                          @Value("${quantum.facebook.page}") String page,
                          @Value("${quantum.facebook.fields}") String fields) {
        this.facebook = facebook;
        this.page = page;
        this.fields = fields;
    }

    public List<FacebookPost> getLatestPosts(int numberOfPosts) {
        try {
            return facebook.getPosts(page, new Reading()
                .limit(numberOfPosts)
                .order(Ordering.REVERSE_CHRONOLOGICAL)
                .fields(fields))
                .stream()
                .map(post -> FacebookPost.builder()
                    .message(post.getMessage())
                    .comments(post.getComments().stream().map(Comment::getMessage).collect(toList()))
                    .build())
                .collect(toList());
        } catch (FacebookException e) {
            log.error("Exception from Facebook: ", e);
            throw new RuntimeException(e);
        }
    }
}
