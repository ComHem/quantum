package se.comhem.quantum.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
    path = "/api/facebook",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FacebookController {

    @Autowired
    private FacebookClient facebookClient;

    @GetMapping
    public List<FacebookPost> findPosts() {
        return facebookClient.getLatestPosts(5);
    }
}
