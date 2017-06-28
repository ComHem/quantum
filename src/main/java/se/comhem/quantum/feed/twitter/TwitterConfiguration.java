package se.comhem.quantum.feed.twitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Slf4j
@Configuration
class TwitterConfiguration {

    private final TwitterFactory twitterFactory;

    TwitterConfiguration(@Value("${quantum.twitter.consumerKey}") String consumerKey,
                         @Value("${quantum.twitter.consumerSecret}") String consumerSecret,
                         @Value("${quantum.twitter.accessToken}") String accessToken,
                         @Value("${quantum.twitter.accessTokenSecret}") String accessTokenSecret) {
        log.info("Configuring Twitter service");
        twitterFactory = new TwitterFactory(new ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
            .build());
    }

    @Bean
    Twitter twitter() {
        return twitterFactory.getInstance();
    }
}