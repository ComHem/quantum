package se.comhem.quantum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    public static final Long serialVersionUID = 11L;

    private String id;
    private LocalDateTime date;
    private LocalDateTime updateDate;
    private Platform platform;
    private String message;
    private String messageImg;
    private String author;
    private String authorImg;
    private String contentLink;
    private String place;
    private List<Double> location;
    @Singular
    private Map<String, Long> reactions;
    @Singular
    private List<Post> replies;

    @JsonProperty("key")
    public String getKey() {
        return format("%s_%s", platform.toString().toLowerCase(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!id.equals(post.id)) return false;
        if (!date.equals(post.date)) return false;
        if (!updateDate.equals(post.updateDate)) return false;
        if (platform != post.platform) return false;
        if (!reactions.equals(post.reactions)) return false;
        return replies.equals(post.replies);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + updateDate.hashCode();
        result = 31 * result + platform.hashCode();
        result = 31 * result + reactions.hashCode();
        result = 31 * result + replies.hashCode();
        return result;
    }
}