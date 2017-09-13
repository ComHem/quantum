package se.comhem.quantum.integration.eventhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ServiceBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Post;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EventHubWriteService {

    public static final int MAX_MESSAGE_SIZE = 1024 * 256;
    private final EventHubClient eventHubWriteClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventHubWriteService(@Qualifier("eventHubWriteClient") EventHubClient eventHubWriteClient,
                                ObjectMapper objectMapper) {
        this.eventHubWriteClient = eventHubWriteClient;
        this.objectMapper = objectMapper;
    }

    public void send(List<Post> posts) {
        List<EventData> events = posts.stream()
            .flatMap(this::createEvent)
            .filter(this::withinMessageSizeLimit)
            .collect(toList());
        if (events.isEmpty()) {
            throw new RuntimeException("Failed to create events  all posts");
        }
        int numberOfSentEvents = events.stream()
            .mapToInt(event -> {
                try {
                    eventHubWriteClient.sendSync(event);
                    return 1;
                } catch (ServiceBusException e) {
                    log.error("Failed to send event batch", e);
                    return 0;
                }
            }).sum();
        log.info("Successfully sent {} events (based on {} posts)", numberOfSentEvents, posts.size());
    }

    private boolean withinMessageSizeLimit(EventData eventData) {
        if (eventData.getBytes().length > MAX_MESSAGE_SIZE) {
            log.warn("Message for post '{}' exceeded max size, will be skipped.", eventData.getProperties().get("id"));
            return false;
        }
        return true;
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
