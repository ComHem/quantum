package se.comhem.quantum.feed.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.FeedDto;
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

    public FeedDto getTweets() throws TwitterException {

        return getFeedDtoMOCK();
//        Query query = new Query("#comhem OR #comhemab OR @comhemab OR @comhem to:comhemab -filter:retweets").resultType(Query.ResultType.recent);
//        query.count(10);
//        QueryResult result = twitter.search(query);
//        List<PostDto> postDtos = mapAllData(result);
//        FeedDto feed = mapToFeed(postDtos);
//        return feed;
    }

    private FeedDto getFeedDtoMOCK() {
        return FeedDto.builder()
                .singles(asList(
                        PostDto.builder()
                                .message("inlägg utan kommentar")
                                .author("Ella eliasson")
                                .id(234L)
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .date("2017-02-01")
                                .plattform("TWITTER")
                                .city("stockholm")
                                .location(asList(64.0, 15.0))
                                .build(),
                        PostDto.builder()
                                .message("inlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentar")
                                .author("Ella eliasson")
                                .id(234L)
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .date("2017-02-01")
                                .location(asList(60.0, 16.0))
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg2 utan kommentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .date("2017-02-01")
                                .location(asList(66.0, 16.0))
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg utan kommentar")
                                .author("Ella eliasson")
                                .id(234L)
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .date("2017-02-01")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentar")
                                .author("Ella eliasson")
                                .id(234L)
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .date("2017-02-01")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg2 utan kommentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .date("2017-02-01")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentar")
                                .author("Ella eliasson")
                                .id(234L)
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .date("2017-02-01")
                                .plattform("TWITTER")
                                .location(asList(66.0, 16.0))
                                .build(),
                        PostDto.builder()
                                .message("inlägg2 utan kommentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .date("2017-02-01")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg2mentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .date("2017-02-01")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg2 utan kommentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .location(asList(60.0, 11.0))
                                .date("2017-02-01")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägnlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentarinlägg utan kommentar")
                                .author("Ella eliasson")
                                .id(234L)
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .date("2017-02-01")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg2 utan kommentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .date("2017-02-01")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg2mentar")
                                .author("rogge Jönsson")
                                .id(234L)
                                .date("2017-02-01")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .plattform("TWITTER")
                                .build()

                )).threads(asList(
                        PostDto.builder()
                                .message("inlägg om smågrodor med kommentar")
                                .author("Ella eliasson")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .id(234L)
                                .replies(asList(
                                        PostDto.builder()
                                                .message("toplevel med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("inss till toplevel")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .location(asList(61.0, 16.0))
                                                        .replies(Collections.emptyList())
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build(),

                                        PostDto.builder()
                                                .message("COMMENT inlägg om smågrodor med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("COMMENT nr 2 inlägg om smågrodor med kommentar")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(asList(PostDto.builder()
                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                .author("rogge Jönsson")
                                                                .id(234L)
                                                                .location(asList(65.0, 16.0))
                                                                .replies(asList(PostDto.builder()
                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                        .author("rogge Jönsson")
                                                                        .id(234L)
                                                                        .replies(asList(PostDto.builder()
                                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                .author("rogge Jönsson")
                                                                                .id(234L)
                                                                                .replies(asList(PostDto.builder()
                                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                        .author("rogge Jönsson")
                                                                                        .id(234L)
                                                                                        .replies(Collections.emptyList())
                                                                                        .plattform("TWITTER")
                                                                                        .build()))
                                                                                .plattform("TWITTER")
                                                                                .build()))
                                                                        .plattform("TWITTER")
                                                                        .build()))
                                                                .plattform("TWITTER")
                                                                .build()))
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build()))
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg om smågrodor med kommentar")
                                .author("Ella eliasson")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .id(234L)
                                .replies(asList(
                                        PostDto.builder()
                                                .message("toplevel med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("inss till toplevel")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(Collections.emptyList())
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build(),

                                        PostDto.builder()
                                                .message("COMMENT inlägg om smågrodor med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("COMMENT nr 2 inlägg om smågrodor med kommentar")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(asList(PostDto.builder()
                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                .author("rogge Jönsson")
                                                                .id(234L)
                                                                .replies(asList(PostDto.builder()
                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                        .author("rogge Jönsson")
                                                                        .id(234L)
                                                                        .replies(asList(PostDto.builder()
                                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                .author("rogge Jönsson")
                                                                                .id(234L)
                                                                                .replies(asList(PostDto.builder()
                                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                        .author("rogge Jönsson")
                                                                                        .id(234L)
                                                                                        .replies(Collections.emptyList())
                                                                                        .plattform("TWITTER")
                                                                                        .build()))
                                                                                .plattform("TWITTER")
                                                                                .build()))
                                                                        .plattform("TWITTER")
                                                                        .build()))
                                                                .plattform("TWITTER")
                                                                .build()))
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build()))
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg om smågrodor med kommentar")
                                .author("Ella eliasson")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .id(234L)
                                .replies(asList(
                                        PostDto.builder()
                                                .message("toplevel med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("inss till toplevel")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(Collections.emptyList())
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build(),

                                        PostDto.builder()
                                                .message("COMMENT inlägg om smågrodor med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("COMMENT nr 2 inlägg om smågrodor med kommentar")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(asList(PostDto.builder()
                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                .author("rogge Jönsson")
                                                                .id(234L)
                                                                .replies(asList(PostDto.builder()
                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                        .author("rogge Jönsson")
                                                                        .id(234L)
                                                                        .replies(asList(PostDto.builder()
                                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                .author("rogge Jönsson")
                                                                                .id(234L)
                                                                                .replies(asList(PostDto.builder()
                                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                        .author("rogge Jönsson")
                                                                                        .id(234L)
                                                                                        .replies(Collections.emptyList())
                                                                                        .plattform("TWITTER")
                                                                                        .build()))
                                                                                .plattform("TWITTER")
                                                                                .build()))
                                                                        .plattform("TWITTER")
                                                                        .build()))
                                                                .plattform("TWITTER")
                                                                .build()))
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build()))
                                .plattform("TWITTER")
                                .build(),
                        PostDto.builder()
                                .message("inlägg om smågrodor med kommentar")
                                .author("Ella eliasson")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .id(234L)
                                .replies(asList(
                                        PostDto.builder()
                                                .message("toplevel med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("inss till toplevel")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(Collections.emptyList())
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build(),

                                        PostDto.builder()
                                                .message("COMMENT inlägg om smågrodor med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("COMMENT nr 2 inlägg om smågrodor med kommentar")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(asList(PostDto.builder()
                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                .author("rogge Jönsson")
                                                                .id(234L)
                                                                .replies(asList(PostDto.builder()
                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                        .author("rogge Jönsson")
                                                                        .id(234L)
                                                                        .replies(asList(PostDto.builder()
                                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                .author("rogge Jönsson")
                                                                                .id(234L)
                                                                                .replies(asList(PostDto.builder()
                                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                        .author("rogge Jönsson")
                                                                                        .id(234L)
                                                                                        .replies(Collections.emptyList())
                                                                                        .plattform("TWITTER")
                                                                                        .build()))
                                                                                .plattform("TWITTER")
                                                                                .build()))
                                                                        .plattform("TWITTER")
                                                                        .build()))
                                                                .plattform("TWITTER")
                                                                .build()))
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build()))
                                .plattform("TWITTER")
                        .build(),
                        PostDto.builder()
                                .message("inlägg om smågrodor med kommentar")
                                .author("Ella eliasson")
                                .autorImg("https://pbs.twimg.com/profile_images/852492247224520705/P1iDZA9a_400x400.jpg")
                                .id(234L)
                                .replies(asList(
                                        PostDto.builder()
                                                .message("toplevel med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("inss till toplevel")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(Collections.emptyList())
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build(),

                                        PostDto.builder()
                                                .message("COMMENT inlägg om smågrodor med kommentar")
                                                .author("rogge Jönsson")
                                                .id(234L)
                                                .replies(asList(PostDto.builder()
                                                        .message("COMMENT nr 2 inlägg om smågrodor med kommentar")
                                                        .author("rogge Jönsson")
                                                        .id(234L)
                                                        .replies(asList(PostDto.builder()
                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                .author("rogge Jönsson")
                                                                .id(234L)
                                                                .replies(asList(PostDto.builder()
                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                        .author("rogge Jönsson")
                                                                        .id(234L)
                                                                        .replies(asList(PostDto.builder()
                                                                                .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                .author("rogge Jönsson")
                                                                                .id(234L)
                                                                                .replies(asList(PostDto.builder()
                                                                                        .message("qrewfq43g534g55ftw4t till smågrodor")
                                                                                        .author("rogge Jönsson")
                                                                                        .id(234L)
                                                                                        .replies(Collections.emptyList())
                                                                                        .plattform("TWITTER")
                                                                                        .build()))
                                                                                .plattform("TWITTER")
                                                                                .build()))
                                                                        .plattform("TWITTER")
                                                                        .build()))
                                                                .plattform("TWITTER")
                                                                .build()))
                                                        .plattform("TWITTER")
                                                        .build()))
                                                .plattform("TWITTER")
                                                .build()))
                                .plattform("TWITTER")
                                .build()))
                .build();
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

    private List<PostDto> mapAllData(QueryResult result) {
        return result.getTweets().stream()
                .filter(status -> status.getInReplyToStatusId() == -1)
                .map(status -> PostDto.builder()
                        .message(status.getText())
                        .author(status.getUser().getName())
                        .city(status.getUser().getLocation())
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

    public List<PostDto> getReplies(Status status) {
        ArrayList<Status> replies2 = fetchReplies(status.getUser().getScreenName(), status.getId());
        return mapToPostDtos(replies2);
    }

    private List<PostDto> mapToPostDtos(ArrayList<Status> replies2) {
        return replies2.stream().map(status ->
                PostDto.builder()
                        .author(status.getUser().getName())
                        .message(status.getText())
                        .replies(getReplies(status))
                        .build()
        ).collect(Collectors.toList());
    }

    public ArrayList<Status> fetchReplies(String screenName, long tweetID) {
        ArrayList<Status> replies = new ArrayList<>();

        try {
            Query query = new Query("to:" + screenName + " since_id:" + tweetID);
            query.count(5);
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
