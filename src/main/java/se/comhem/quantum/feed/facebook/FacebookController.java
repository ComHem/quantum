package se.comhem.quantum.feed.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.comhem.quantum.feed.FeedDto;

@RestController
@RequestMapping(
    path = "/api/facebook",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FacebookController {

    @Autowired
    private FacebookClient facebookClient;

    @GetMapping
    public FeedDto findPosts() {

        return facebookClient.getLatestPosts(5);
    }
}