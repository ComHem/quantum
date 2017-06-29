package se.comhem.quantum.feed.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Ordering;
import facebook4j.Reading;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.FeedDto;
import se.comhem.quantum.feed.twitter.PostDto;

import java.util.List;
import java.util.stream.Collectors;

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
        return mapToFeed(postDtos);
    }

    private FeedDto mapToFeed(List<PostDto> postDtos) {
        FeedDto feed = new FeedDto();
        feed.setSingles(
                postDtos.stream()
                        .filter(postDto -> postDto.getReplies().isEmpty())
                        .collect(Collectors.toList()));
        feed.setThreads(
                postDtos.stream()
                        .filter(postDto -> !postDto.getReplies().isEmpty())
                        .collect(Collectors.toList()));
        return feed;

    }

    private List<PostDto> fetchPosts(int numberOfPosts) {
        try {
            return facebook.getPosts(page, new Reading()
                    .limit(numberOfPosts)
                    .order(Ordering.REVERSE_CHRONOLOGICAL)
                    .fields(fields))
                    .stream()
                    .map(post -> PostDto.builder()
                            .message(post.getMessage())
                            .plattform("facebook")
                            .author(post.getName())
                            .autorImg(post.getPicture().getPath())
                            .replies(post.getComments().stream()
                                    .map(comment -> PostDto.builder()
                                            .message(comment.getMessage())
                                            .author(comment.getFrom().getName())
                                            .build())
                                    .collect(toList()))
                            .build())
                    .collect(toList());
        } catch (FacebookException e) {
            log.error("Exception from Facebook: ", e);
            throw new RuntimeException(e);
        }
    }
}
