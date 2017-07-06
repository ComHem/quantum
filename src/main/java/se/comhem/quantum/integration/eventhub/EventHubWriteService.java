package se.comhem.quantum.integration.eventhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ServiceBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Post;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EventHubWriteService {

    private final EventHubClient eventHubWriteClient;
    private final String partition;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventHubWriteService(@Qualifier("eventHubWriteClient") EventHubClient eventHubWriteClient,
                                @Value("${quantum.eventhub.partition}") String partition,
                                ObjectMapper objectMapper) {
        this.eventHubWriteClient = eventHubWriteClient;
        this.partition = partition;
        this.objectMapper = objectMapper;
    }

    public void send(List<Post> posts) {
        List<EventData> events = posts.stream()
            .flatMap(this::createEvent)
            .collect(toList());
        if (events.isEmpty()) {
            throw new RuntimeException("Failed to create events  all posts");
        }
        int numberOfSentEvents = Lists.partition(events, 10).stream()
            .mapToInt(eventBatch -> {
                try {
                    eventHubWriteClient.sendSync(eventBatch, partition);
                    return eventBatch.size();
                } catch (ServiceBusException e) {
                    log.error("Failed to send event batch", e);
                    return 0;
                }
            }).sum();
        log.info("Successfully sent {} events (based on {} posts)", numberOfSentEvents, posts.size());
    }

    private Stream<EventData> createEvent(Post post) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(post);
            EventData eventData = new EventData(bytes);
            eventData.getProperties().put("id", post.getId());
            eventData.getProperties().put("platform", post.getPlatform().toString());
            eventData.getProperties().put("serialVersionUID", Post.serialVersionUID);
            return Stream.of(eventData);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize post: " + post.getId(), e);
            return Stream.empty();
        }
    }
}
