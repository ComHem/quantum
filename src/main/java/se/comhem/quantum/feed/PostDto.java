package se.comhem.quantum.feed;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    Long id;
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
