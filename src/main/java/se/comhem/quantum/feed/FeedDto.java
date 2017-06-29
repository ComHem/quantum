package se.comhem.quantum.feed;

import lombok.*;
import se.comhem.quantum.feed.twitter.PostDto;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedDto {
    List<PostDto> threads;
    List<PostDto> singles;
}
