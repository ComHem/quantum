package se.comhem.quantum.feed.facebook;

import facebook4j.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.FeedDto;
import se.comhem.quantum.feed.Mapper;
import se.comhem.quantum.feed.PostDto;

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

    public FeedDto getLatestPosts(int numberOfPosts) {
        List<PostDto> postDtos = fetchPosts(numberOfPosts);
        return Mapper.mapToFeed(postDtos);
    }



    private List<PostDto> fetchPosts(int numberOfPosts) {
        try {
            return facebook.getPosts(page, new Reading()
                    .limit(numberOfPosts)
                    .order(Ordering.REVERSE_CHRONOLOGICAL)
                    .fields(fields))
                    .stream()
                    .map(post -> Mapper.mapFacebookPostToPostDto(getProfilePicPath(post), post))
                    .collect(toList());
        } catch (FacebookException e) {
            log.error("Exception from Facebook: ", e);
            throw new RuntimeException(e);
        }
    }

    private String getProfilePicPath(Post post) {
        try {
            return facebook.getPictureURL(post.getFrom().getId()).toString();
        } catch (FacebookException e) {
            e.printStackTrace();
        }
        return "";
    }
}
