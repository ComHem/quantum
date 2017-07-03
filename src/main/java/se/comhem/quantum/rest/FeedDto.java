package se.comhem.quantum.rest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class FeedDto {
    @Singular
    private List<PostDto> threads;

    @Singular
    private List<PostDto> singles;
}
