package se.comhem.quantum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    public static final Long serialVersionUID = 2L;

    private String id;
    private String date;
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
}