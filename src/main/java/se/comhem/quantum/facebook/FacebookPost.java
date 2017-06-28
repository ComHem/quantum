package se.comhem.quantum.facebook;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class FacebookPost {
    private String message;
    private List<String> comments;
}
