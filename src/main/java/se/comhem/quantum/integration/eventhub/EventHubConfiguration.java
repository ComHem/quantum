package se.comhem.quantum.integration.eventhub;

import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ServiceBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class EventHubConfiguration {

    private final String writeClientConnectionString;
    private final String readClientConnectionString;

    EventHubConfiguration(@Value("${quantum.eventhub.writeConnectionString}") String writeClientConnectionString,
                          @Value("${quantum.eventhub.readConnectionString}") String readClientConnectionString) {
        this.writeClientConnectionString = writeClientConnectionString;
        this.readClientConnectionString = readClientConnectionString;
    }

    @Bean(name = "eventHubWriteClient")
    EventHubClient eventHubWriteClient() throws IOException, ServiceBusException, InvalidKeyException, NoSuchAlgorithmException {
        log.info("Configuring EventHub write client");
        return EventHubClient.createFromConnectionStringSync(writeClientConnectionString);
    }

    @Bean(name = "eventHubReadClient")
    EventHubClient eventHubReadClient() throws IOException, ServiceBusException, InvalidKeyException, NoSuchAlgorithmException {
        log.info("Configuring EventHub read client");
        return EventHubClient.createFromConnectionStringSync(readClientConnectionString);
    }
}
