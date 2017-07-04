package se.comhem.quantum.integration.twitter;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.geocode.GeoCodeService;
import se.comhem.quantum.model.Platform;
import se.comhem.quantum.model.Post;
import se.comhem.quantum.util.DateUtils;
import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class TwitterService {

    private final Twitter twitter;
    private final GeoCodeService geoCodeService;

    @Autowired
    public TwitterService(Twitter twitter, GeoCodeService geoCodeService) {
        this.twitter = twitter;
        this.geoCodeService = geoCodeService;
    }

    public List<Post> getTweets(int numberOfTweets) {
        try {
            long start = System.currentTimeMillis();
            QueryResult result = fetchData(numberOfTweets);
            log.info("{}", result.getRateLimitStatus().toString());
            List<Post> posts = mapTwitterStatuses(result);
            log.info("Took {} ms to fetch {} tweets", System.currentTimeMillis() - start, posts.size());
            return posts;
        } catch (TwitterException e) {
            log.error("Exception from Facebook: ", e);
            throw new RuntimeException(e);
        }
    }

    private QueryResult fetchData(int numberOfTweets) throws TwitterException {
        Query query = new Query("#comhem OR #comhemab OR @comhemab OR comhemab OR comhem OR @comhem to:comhemab from:comhemab -filter:retweets").resultType(Query.ResultType.recent);
        query.count(numberOfTweets);
        return twitter.search(query);
    }


    private List<Post> mapTwitterStatuses(QueryResult result) {
        return result.getTweets().stream()
            .filter(status -> status.getInReplyToStatusId() == -1)
            .map(status -> Post.builder()
                .id(String.valueOf(status.getId()))
                .message(status.getText())
                .author(status.getUser().getName())
                .authorImg(status.getUser().getBiggerProfileImageURL())
                .city(status.getUser().getLocation())
                .location(getGeo(status))
                .contentLink(getMediaIfExists(status))
                .platform(Platform.TWITTER)
                .date(DateUtils.fromDate(status.getCreatedAt()))
                .updateDate(DateUtils.fromDate(status.getCreatedAt()))
                .replies(getReplies(status))
                .reactions(ImmutableMap.of(
                    "RETWEET", (long) status.getRetweetCount(),
                    "FAVORITE", (long) status.getFavoriteCount()
                ))
                .build())
            .collect(toList());
    }

    private String getMediaIfExists(Status status) {
        return Arrays.stream(status.getMediaEntities())
                .map(MediaEntity::getMediaURL)
                .findFirst()
                .orElse("");
    }

    private List<Double> getGeo(Status status) {
        return Optional.ofNullable(status.getGeoLocation())
                .map(geoLocation -> asList(geoLocation.getLatitude(), geoLocation.getLongitude()))
                .orElseGet(() -> getGeoFromCity(status.getUser().getLocation()));
    }

    private List<Double> getGeoFromCity(String cityName) {
        return geoCodeService.getGeoLocation(cityName);
    }

    private List<Post> getReplies(Status status) {
        ArrayList<Status> replies = fetchReplies(status.getUser().getScreenName(), status.getId());
        return mapTwitterRepliesToPosts(replies);
    }

    private List<Post> mapTwitterRepliesToPosts(ArrayList<Status> resplies) {
        return resplies.stream().map(status ->
            Post.builder()
                .id(String.valueOf(status.getId()))
                .platform(Platform.TWITTER)
                .author(status.getUser().getName())
                .authorImg(status.getUser().getBiggerProfileImageURL())
                .message(status.getText())
                .date(DateUtils.fromDate(status.getCreatedAt()))
                .updateDate(DateUtils.fromDate(status.getCreatedAt()))
                .city(status.getUser().getLocation())
                .location(getGeo(status))
                .reactions(ImmutableMap.of(
                    "RETWEET", (long) status.getRetweetCount(),
                    "FAVORITE", (long) status.getFavoriteCount()
                ))
                .build()
        ).collect(toList());
    }

    private ArrayList<Status> fetchReplies(String screenName, long tweetID) {
        ArrayList<Status> replies = new ArrayList<>();

        try {
            Query query = new Query("to:" + screenName + " since_id:" + tweetID);
            query.count(1);
            QueryResult results;

            do {
                results = twitter.search(query);
                log.info("{}", results.getRateLimitStatus().toString());
                List<Status> tweets = results.getTweets();
                for (Status tweet : tweets)
                    if (tweet.getInReplyToStatusId() == tweetID)
                        replies.add(tweet);
            } while ((query = results.nextQuery()) != null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return replies;
    }
}
