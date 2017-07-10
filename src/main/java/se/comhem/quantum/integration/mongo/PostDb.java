package se.comhem.quantum.integration.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import se.comhem.quantum.model.Platform;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDb {
    @Id
    private String id;
    private LocalDateTime date;
    private LocalDateTime updateDate;
    private Platform platform;
    private String message;
    private String messageImg;
    private String author;
    private String authorId;
    private String authorImg;
    private String contentLink;
    private String place;
    private List<Double> location;
    private Map<String, Long> reactions;
    private List<PostDb> replies;
}
