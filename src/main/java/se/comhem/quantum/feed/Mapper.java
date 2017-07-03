package se.comhem.quantum.feed;

import facebook4j.Post;
import twitter4j.Status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Mapper {

    public static PostDto mapFacebookPostToPostDto(String commentLocation, String picUrl, Post post) {
        return PostDto.builder()
                .message(post.getMessage())
                .authorImg(picUrl)
                .plattform("facebook")
                .city(commentLocation)
                .reactions(post.getReactions().stream().map(reaction -> reaction.getType().toString()).collect(toList()))
                .contentLink(Optional.ofNullable(post.getAttachments())
                        .map(attachments -> attachments.stream()
                                .findFirst()
                                .map(Post.Attachment::getUrl).orElse(""))
                        .orElse(""))
                .date(Optional.ofNullable(post.getUpdatedTime())

                        .map(Date::toString)
                        .orElse(""))
                .author(post.getFrom().getName())
                .replies(post.getComments().stream()
                        .map(comment -> PostDto.builder()
                                .message(comment.getMessage())
                                .author(comment.getFrom().getName())
                                .date(Optional.ofNullable(comment.getCreatedTime())
                                        .map(Mapper::formatDate)
                                        .orElse(""))
                                .build())
                        .collect(toList()))
                .build();
    }

    private static String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
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
