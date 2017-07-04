package se.comhem.quantum.integration.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Ordering;
import facebook4j.Reading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Platform;
import se.comhem.quantum.model.Post;
import se.comhem.quantum.util.DateUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FacebookService {

    private final Facebook facebook;
    private final String page;
    private final String fields;

    @Autowired
    public FacebookService(Facebook facebook,
                           @Value("${quantum.facebook.page}") String page,
                           @Value("${quantum.facebook.fields}") String fields) {
        this.facebook = facebook;
        this.page = page;
        this.fields = fields;
    }

    public List<Post> getLatestPosts(int numberOfPosts) {
        try {
            long start = System.currentTimeMillis();
            List<Post> posts = facebook.getPosts(page, options(numberOfPosts)).stream()
                .map(this::mapFacebookPost)
                .collect(toList());
            log.info("Took {} ms to fetch {} facebook posts", System.currentTimeMillis() - start, posts.size());
            return posts;
        } catch (FacebookException e) {
            log.error("Exception from Facebook: ", e);
            throw new RuntimeException(e);
        }
    }

    private Reading options(int numberOfPosts) {
        return new Reading()
            .limit(numberOfPosts)
            .order(Ordering.REVERSE_CHRONOLOGICAL)
            .fields(fields);
    }

    private Post mapFacebookPost(facebook4j.Post post) {
        return Post.builder()
            .id(post.getId())
            .message(post.getMessage())
            .authorImg(getProfilePicPath(post.getFrom().getId()))
            .platform(Platform.FACEBOOK)
            .reactions(getReactions(post))
            .contentLink(Optional.ofNullable(post.getAttachments())
                .flatMap(attachments -> attachments.stream()
                    .findFirst()
                    .map(facebook4j.Post.Attachment::getUrl))
                .orElse(""))
            .date(DateUtils.fromDate(post.getCreatedTime()))
            .updateDate(DateUtils.fromDate(post.getUpdatedTime()))
            .author(post.getFrom().getName())
            .location(Arrays.asList(59.33319939999999, 18.0444084)) // TODO: Hardcoded to Stockholm right now
            .replies(post.getComments().stream()
                .map(comment -> Post.builder()
                    .id(comment.getId())
                    .platform(Platform.FACEBOOK)
                    .message(comment.getMessage())
                    .author(comment.getFrom().getName())
                    .date(DateUtils.fromDate(comment.getCreatedTime()))
                    .updateDate(DateUtils.fromDate(comment.getCreatedTime()))
                    .build())
                .collect(toList()))
            .build();
    }

    private Map<String, Long> getReactions(facebook4j.Post post) {
        return post.getReactions().stream()
            .map(reaction -> reaction.getType().toString())
            .collect(groupingBy(Function.identity(), Collectors.counting()));
    }

    private String getProfilePicPath(String userId) {
        try {
            return facebook.getPictureURL(userId).toString();
        } catch (FacebookException e) {
            return null;
        }
    }
}
