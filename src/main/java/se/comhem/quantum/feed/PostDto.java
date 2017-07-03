package se.comhem.quantum.feed;

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
public class PostDto {
    public static final Long serialVersionUID = 1L;

    String id;
    String date;
    String plattform;
    String message;
    String messageImg;
    String author;
    String authorImg;
    String contentLink;
    List<PostDto> replies;
    List<Double> location;
    String city;
    List<String> reactions;
}
