package se.comhem.quantum.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    public void testEquals() throws Exception {
        LocalDateTime createdDate = LocalDateTime.now().minusHours(2);
        LocalDateTime updateDate = LocalDateTime.now();
        Post post = Post.builder()
            .id("1234")
            .platform(Platform.FACEBOOK)
            .date(createdDate)
            .updateDate(updateDate)
            .message("Post message")
            .reaction("Like", 10L)
            .reaction("Sad", 15L)
            .reply(Post.builder()
                .id("1234-1234")
                .platform(Platform.FACEBOOK)
                .date(updateDate)
                .updateDate(updateDate)
                .reaction("Like", 5L)
                .reaction("Sad", 7L)
                .message("Reply message")
                .build())
            .build();
        Post equalPost = Post.builder()
            .id("1234")
            .platform(Platform.FACEBOOK)
            .date(createdDate)
            .updateDate(updateDate)
            .message("Post message")
            .reaction("Sad", 15L)
            .reaction("Like", 10L)
            .reply(Post.builder()
                .id("1234-1234")
                .platform(Platform.FACEBOOK)
                .date(updateDate)
                .updateDate(updateDate)
                .reaction("Sad", 7L)
                .reaction("Like", 5L)
                .message("Reply message")
                .build())
            .build();

        assertThat(post.equals(equalPost)).isTrue();
    }

    @Test
    public void testNotEqualsWhenReactionsChanges() throws Exception {
        LocalDateTime createdDate = LocalDateTime.now().minusHours(2);
        LocalDateTime updateDate = LocalDateTime.now();
        Post post = Post.builder()
            .id("1234")
            .platform(Platform.FACEBOOK)
            .date(createdDate)
            .updateDate(updateDate)
            .message("Post message")
            .reaction("Like", 10L)
            .reaction("Sad", 15L)
            .reply(Post.builder()
                .id("1234-1234")
                .platform(Platform.FACEBOOK)
                .date(updateDate)
                .updateDate(updateDate)
                .reaction("Like", 10L)
                .reaction("Sad", 15L)
                .message("Reply message")
                .build())
            .build();
        Post updatedReactionsPost = Post.builder()
            .id("1234")
            .platform(Platform.FACEBOOK)
            .date(createdDate)
            .updateDate(updateDate)
            .message("Post message")
            .reaction("Like", 10L)
            .reaction("Sad", 15L)
            .reply(Post.builder()
                .id("1234-1234")
                .platform(Platform.FACEBOOK)
                .date(updateDate)
                .updateDate(updateDate)
                .reaction("Like", 15L)
                .reaction("Sad", 15L)
                .message("Reply message")
                .build())
            .build();

        assertThat(post.equals(updatedReactionsPost)).isFalse();
    }

    @Test
    public void testNotEqualsWhenRepliesChanges() throws Exception {
        LocalDateTime createdDate = LocalDateTime.now().minusHours(2);
        LocalDateTime replyCreatedDate = LocalDateTime.now().minusMinutes(5);
        LocalDateTime reply2CreatedDate = LocalDateTime.now();
        Post post = Post.builder()
            .id("1234")
            .platform(Platform.TWITTER)
            .date(createdDate)
            .updateDate(createdDate)
            .message("Post message")
            .reaction("Like", 10L)
            .reaction("Sad", 15L)
            .reply(Post.builder()
                .id("1234-1234")
                .platform(Platform.TWITTER)
                .date(replyCreatedDate)
                .updateDate(replyCreatedDate)
                .reaction("Like", 10L)
                .reaction("Sad", 15L)
                .message("Reply message")
                .build())
            .build();
        Post updatedRepliesPost = Post.builder()
            .id("1234")
            .platform(Platform.TWITTER)
            .date(createdDate)
            .updateDate(createdDate)
            .message("Post message")
            .reaction("Like", 10L)
            .reaction("Sad", 15L)
            .reply(Post.builder()
                .id("1234-1234")
                .platform(Platform.TWITTER)
                .date(replyCreatedDate)
                .updateDate(replyCreatedDate)
                .reaction("Like", 10L)
                .reaction("Sad", 15L)
                .message("Reply message")
                .build())
            .reply(Post.builder()
                .id("1234-9876")
                .platform(Platform.TWITTER)
                .date(reply2CreatedDate)
                .updateDate(reply2CreatedDate)
                .message("Reply message")
                .build())
            .build();

        assertThat(post.equals(updatedRepliesPost)).isFalse();
    }
}