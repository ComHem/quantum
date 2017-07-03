package se.comhem.quantum.feed;


import lombok.*;

import java.util.Collections;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedDto {
    List<PostDto> threads = Collections.emptyList();
    List<PostDto> singles = Collections.emptyList();
}
