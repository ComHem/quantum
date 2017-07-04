package se.comhem.quantum.integration.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Ordering;
import facebook4j.Reading;
import facebook4j.ResponseList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Platform;
import se.comhem.quantum.model.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
            ResponseList<facebook4j.Post> posts = facebook.getPosts(page, new Reading()
                    .limit(numberOfPosts)
                    .order(Ordering.REVERSE_CHRONOLOGICAL)
                    .fields(fields));
            return posts.stream()
                    .map(post -> mapFacebookPost(getProfilePicPath(post), post))
                    .collect(toList());
        } catch (FacebookException e) {
            log.error("Exception from Facebook: ", e);
            throw new RuntimeException(e);
        }
    }

    private String getProfilePicPath(facebook4j.Post post) {
        try {
            return facebook.getPictureURL(post.getFrom().getId()).toString();
        } catch (FacebookException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Post mapFacebookPost(String picUrl, facebook4j.Post post) {
        return Post.builder()
                .message(post.getMessage())
                .authorImg(picUrl)
                .platform(Platform.FACEBOOK)
                .reactions(post.getReactions().stream().map(reaction -> reaction.getType().toString()).collect(toList()))
                .contentLink(Optional.ofNullable(post.getAttachments())
                        .flatMap(attachments -> attachments.stream()
                                .findFirst()
                                .map(facebook4j.Post.Attachment::getUrl))
                        .orElse(""))
                .date(Optional.ofNullable(post.getUpdatedTime())
                        .map(Date::toString)
                        .orElse(""))
                .author(post.getFrom().getName())
                .replies(post.getComments().stream()
                        .map(comment -> Post.builder()
                                .message(comment.getMessage())
                                .author(comment.getFrom().getName())
                                .id(comment.getFrom().getId())
                                .reactions(post.getReactions().stream().map(reaction -> reaction.getType().toString()).collect(toList()))
                                .date(Optional.ofNullable(comment.getCreatedTime())
                                        .map(FacebookService::formatDate)
                                        .orElse(""))
                                .build())
                        .collect(toList()))
                .build();
    }

    private static String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}
