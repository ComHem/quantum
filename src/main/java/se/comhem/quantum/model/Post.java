package se.comhem.quantum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    public static final Long serialVersionUID = 6L;

    private String id;
    private LocalDateTime date;
    private LocalDateTime updateDate;
    private Platform platform;
    private String message;
    private String messageImg;
    private String author;
    private String authorImg;
    private String contentLink;
    private String city;
    private List<Double> location;
    private List<String> reactions;
    private List<Post> replies;

    @JsonProperty("key")
    public String getKey() {
        return format("%s_%s", platform.toString().toLowerCase(), id);
    }
}