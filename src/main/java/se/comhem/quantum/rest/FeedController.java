package se.comhem.quantum.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {
    private final FeedService feedService;
    private final String brand;

    @Autowired
    public FeedController(FeedService feedService, @Value("${quantum.brand}") String brand) {
        this.feedService = feedService;
        this.brand = brand;
    }

    @GetMapping(value = "/api/feed")
    public FeedDto findFeed() {
        return feedService.getFeed();
    }

    @GetMapping(value = "/api/brand")
    public String getBrand() {
        return brand;
    }
}