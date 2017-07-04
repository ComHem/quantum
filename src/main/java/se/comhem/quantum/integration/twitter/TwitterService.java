package se.comhem.quantum.integration.twitter;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.integration.geocode.GeoCodeService;
import se.comhem.quantum.model.Platform;
import se.comhem.quantum.model.Post;
import se.comhem.quantum.util.DateUtils;
import twitter4j.GeoLocation;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class TwitterService {

    private static final long TOP_LEVEL_TWEETS = -1L;

    private final Twitter twitter;
    private final GeoCodeService geoCodeService;
    private final Set<String> queries;

    @Autowired
    public TwitterService(Twitter twitter, GeoCodeService geoCodeService, @Value("${quantum.twitter.queries}") String queries) {
        this.twitter = twitter;
        this.geoCodeService = geoCodeService;
        this.queries = Arrays.stream(queries.split(";")).map(q -> q  + " -filter:retweets").collect(toSet());
    }

    public List<Post> getTweets(int numberOfTweetsPerQuery) {
        long start = System.currentTimeMillis();
        List<Status> tweets = fetchData(numberOfTweetsPerQuery);
        Map<Long, List<Status>> tweetsByReplyId = tweets.stream().distinct().collect(groupingBy(Status::getInReplyToStatusId));
        List<Post> posts = tweetsByReplyId.get(TOP_LEVEL_TWEETS).stream()
            .map(status -> mapStatus(status, tweetsByReplyId))
            .collect(toList());
        log.info("Took {} ms to fetch {} tweets", System.currentTimeMillis() - start, posts.size());
        return posts;
    }

    private List<Status> fetchData(int numberOfTweetsPerQuery) {
        return queries.stream()
            .map(query -> new Query(query).resultType(Query.ResultType.recent).count(numberOfTweetsPerQuery))
            .map(query -> {
                try {
                    return twitter.search(query);
                } catch (TwitterException e) {
                    log.error("Exception from Twitter: ", e);
                    throw new RuntimeException(e);
                }
            })
            .flatMap(result -> {
                log.info("{}", result.getRateLimitStatus());
                return result.getTweets().stream();
            })
            .collect(toList());
    }

    private Post mapStatus(Status status, Map<Long, List<Status>> tweetsByReplyId) {
        return Post.builder()
            .id(String.valueOf(status.getId()))
            .message(status.getText())
            .author(status.getUser().getName())
            .authorImg(status.getUser().getBiggerProfileImageURL())
            .place(status.getPlace() != null ? status.getPlace().getFullName() : status.getUser().getLocation())
            .location(getGeo(status))
            .contentLink(getMediaIfExists(status))
            .platform(Platform.TWITTER)
            .date(DateUtils.fromDate(status.getCreatedAt()))
            .updateDate(DateUtils.fromDate(status.getCreatedAt()))
            .replies(tweetsByReplyId.getOrDefault(status.getId(), Collections.emptyList()).stream()
                .map(reply -> mapStatus(reply, tweetsByReplyId))
                .collect(toList()))
            .reactions(ImmutableMap.of(
                "RETWEET", (long) status.getRetweetCount(),
                "FAVORITE", (long) status.getFavoriteCount()
            ))
            .build();
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
            .orElseGet(() -> getGeoFromPlace(status.getPlace()));
//            .orElseGet(() -> getGeoFromCity(status.getUser().getLocation()));
    }

    private List<Double> getGeoFromPlace(Place place) {
        if (place != null && place.getBoundingBoxCoordinates() != null) {
            try {
                // TODO: Figure out something better here
                Double maxLat = Arrays.stream(place.getBoundingBoxCoordinates()[0]).map(GeoLocation::getLatitude).max(Comparator.comparingDouble(value -> value)).get();
                Double minLat = Arrays.stream(place.getBoundingBoxCoordinates()[0]).map(GeoLocation::getLatitude).min(Comparator.comparingDouble(value -> value)).get();
                Double maxLong = Arrays.stream(place.getBoundingBoxCoordinates()[0]).map(GeoLocation::getLongitude).max(Comparator.comparingDouble(value -> value)).get();
                Double minLong = Arrays.stream(place.getBoundingBoxCoordinates()[0]).map(GeoLocation::getLongitude).min(Comparator.comparingDouble(value -> value)).get();
                double centerLatitude = ( minLat + maxLat ) / 2;
                double centerLongitude = ( minLong + maxLong ) / 2;
                return Arrays.asList(centerLatitude, centerLongitude);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private List<Double> getGeoFromCity(String cityName) {
        return geoCodeService.getGeoLocation(cityName); // TODO: Rate limited, may need credentials https://developers.google.com/maps/documentation/geocoding/usage-limits
    }
}
