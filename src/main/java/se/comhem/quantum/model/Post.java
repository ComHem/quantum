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
    public static final Long serialVersionUID = 9L;

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
    @Singular
    private Map<String, Long> reactions;
    @Singular
    private List<Post> replies;

    @JsonProperty("key")
    public String getKey() {
        return format("%s_%s", platform.toString().toLowerCase(), id);
    }
}