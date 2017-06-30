package se.comhem.quantum.feed;

import facebook4j.Post;
import twitter4j.Status;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Mapper {

    public static PostDto mapFacebookPostToPostDto(String picUrl, Post post) {
        return PostDto.builder()
                .message(post.getMessage())
                .authorImg(picUrl)
                .plattform("facebook")
                .contentLink(post.getAttachments().get(0).getUrl())
                .date(Optional.ofNullable(post.getCreatedTime())
                        .map(Date::toString)
                        .orElse(""))
                .author(post.getFrom().getName())
                .replies(post.getComments().stream()
                        .map(comment -> PostDto.builder()
                                .message(comment.getMessage())
                                .author(comment.getFrom().getName())
                                .date(Optional.ofNullable(comment.getCreatedTime())
                                        .map(Date::toString)
                                        .orElse(""))
                                .build())
                        .collect(toList()))
                .build();
    }

    public static FeedDto mapToFeed(List<PostDto> postDtos) {
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

    private static List<Double> getGeo(Status status) {
        return Optional.ofNullable(status.getGeoLocation())
                .map(geoLocation -> asList(geoLocation.getLatitude(), geoLocation.getLongitude()))
                .orElseGet(Collections::emptyList);
    }
}
