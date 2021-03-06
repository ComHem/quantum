package se.comhem.quantum.integration.facebook;

import facebook4j.Category;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FacebookService {

    private static final String FIELDS = "hometown,location,created_time,updated_time,message,comments.limit(1000),properties,reactions.limit(1000),tags,picture,fullPicture,story,storyTags,whiteTags,place,link,name,to,from,icon,actions,sharesCount,statusType,attachments,parentId,user_location,user_hometown";
    private final Facebook facebook;
    private final String page;


    @Autowired
    public FacebookService(Facebook facebook,
                           @Value("${quantum.facebook.page}") String page) {
        this.facebook = facebook;
        this.page = page;
    }

    public List<Post> getLatestPosts(int numberOfPosts) {
        try {
            long start = System.currentTimeMillis();
            List<Post> posts = facebook.getPosts(page, options(numberOfPosts)).stream()
                .flatMap(this::mapFacebookPost)
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
            .filter("stream")
            .summary()
            .order(Ordering.REVERSE_CHRONOLOGICAL)
            .fields(FIELDS);
    }

    private Stream<Post> mapFacebookPost(facebook4j.Post post) {
        try {
            return Stream.of(Post.builder()
                .id(post.getId())
                .message(post.getMessage())
                .author(post.getFrom().getName())
                .authorId(post.getFrom().getId())
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
                .location(Arrays.asList(59.33319939999999, 18.0444084)) // TODO: Hardcoded to Stockholm right now
                .replies(post.getComments().stream()
                    .map(comment -> Post.builder()
                        .id(comment.getId())
                        .platform(Platform.FACEBOOK)
                        .message(comment.getMessage())
                        .author(getOrNull(comment::getFrom, Category::getName))
                        .authorId(getOrNull(comment::getFrom, Category::getId))
                        .date(getOrNull(comment::getCreatedTime, DateUtils::fromDate))
                        .updateDate(getOrNull(comment::getCreatedTime, DateUtils::fromDate))
                        .build())
                    .collect(toList()))
                .build());
        } catch (Exception e) {
            log.error("Unhandled exception while mapping facebook post: " + post.getId(), e);
            return Stream.empty();
        }
    }

    private static <T, R> R getOrNull(Supplier<T> initial, Function<T, R> mapper) {
        return Optional.ofNullable(initial.get()).map(mapper).orElse(null);
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
