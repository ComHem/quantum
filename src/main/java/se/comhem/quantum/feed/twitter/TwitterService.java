package se.comhem.quantum.feed.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.FeedDto;
import se.comhem.quantum.feed.PostDto;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class TwitterService {

    private final Twitter twitter;

    @Autowired
    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
    }

    public FeedDto getTweets() {
//        return FeedMock.getFeedDtoMOCK();
        FeedDto feed = new FeedDto();
        try {
            QueryResult result = fetchData();
            feed = mapToFeed(result);

        } catch (TwitterException e) {
            Logger.getLogger(TwitterService.class).error("e");
        }
        return feed;
    }

    private QueryResult fetchData() throws TwitterException {
        Query query = new Query("#comhem OR #comhemab OR @comhemab OR @comhem to:comhemab -filter:retweets").resultType(Query.ResultType.recent);
        query.count(10);
        return twitter.search(query);
    }


    private FeedDto mapToFeed(QueryResult result) {
        List<PostDto> postDtos = mapTwitterStatuses(result);
        FeedDto feed = new FeedDto();
        feed.setSingles(
                postDtos.stream()
                        .filter(postDto -> postDto.getReplies() == null || postDto.getReplies().isEmpty())
                        .collect(Collectors.toList()));
        feed.setThreads(
                postDtos.stream()
                        .filter(postDto -> postDto.getReplies() != null)
                        .filter(postDto -> !postDto.getReplies().isEmpty())
                        .collect(Collectors.toList()));
        return feed;

    }

    private List<PostDto> mapTwitterStatuses(QueryResult result) {
        return result.getTweets().stream()
                .filter(status -> status.getInReplyToStatusId() == -1)
                .map(status -> PostDto.builder()
                        .message(status.getText())
                        .author(status.getUser().getName())
                        .authorImg(status.getUser().getProfileImageURL())
                        .city(status.getUser().getLocation())
                        .contentLink("")
                        .location(getGeo(status))
                        .id(status.getId())
                        .plattform("TWITTER")
                        .replies(getReplies(status))
                        .build())
                .collect(Collectors.toList());
    }

    private List<Double> getGeo(Status status) {
        return Optional.ofNullable(status.getGeoLocation())
                .map(geoLocation -> asList(geoLocation.getLatitude(), geoLocation.getLongitude()))
                .orElseGet(Collections::emptyList);
    }

    private List<PostDto> getReplies(Status status) {
        ArrayList<Status> replies = fetchReplies(status.getUser().getScreenName(), status.getId());
        return mapToPostDtos(replies);
    }

    private List<PostDto> mapToPostDtos(ArrayList<Status> replies2) {
        return replies2.stream().map(status ->
                PostDto.builder()
                        .author(status.getUser().getName())
                        .message(status.getText())
                        .build()
        ).collect(Collectors.toList());
    }

    private ArrayList<Status> fetchReplies(String screenName, long tweetID) {
        ArrayList<Status> replies = new ArrayList<>();

        try {
            Query query = new Query("to:" + screenName + " since_id:" + tweetID);
            query.count(1);
            QueryResult results;

            do {
                results = twitter.search(query);
                System.out.println("Results: " + results.getTweets().size());
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
