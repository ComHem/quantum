package se.comhem.quantum.integration.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {
    private final String connectionString;

    public MongoConfiguration(@Value("${quantum.mongo.connectionString}") String connectionString) {
        this.connectionString = connectionString;
    }

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(connectionString));
    }
}
