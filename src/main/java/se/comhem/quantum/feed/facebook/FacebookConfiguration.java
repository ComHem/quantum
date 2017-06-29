package se.comhem.quantum.feed.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class FacebookConfiguration {

    private final FacebookFactory facebookFactory;

    FacebookConfiguration(@Value("${quantum.facebook.appId}") String appId,
                          @Value("${quantum.facebook.appSecret}") String appSecret,
                          @Value("${quantum.facebook.accessToken}") String accessToken) {
        log.info("Configuring Facebook service");
        facebookFactory = new FacebookFactory(new ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthAppId(appId)
            .setOAuthAppSecret(appSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthPermissions("email,publish_stream") // TODO: what to do...
            .build());
    }

    @Bean
    Facebook facebook() {
        return facebookFactory.getInstance();
    }
}
