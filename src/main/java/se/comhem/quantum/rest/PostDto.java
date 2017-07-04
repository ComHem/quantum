package se.comhem.quantum.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private String id;
    private String date;
    private String platform;
    private String message;
    private String messageImg;
    private String author;
    private String authorImg;
    private String contentLink;
    private String place;
    private List<Double> location;
    @Singular
    private Set<Entry<String, Long>> reactions;
    @Singular
    private List<PostDto> replies;
}
