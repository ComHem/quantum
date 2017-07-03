package se.comhem.quantum.eventhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.PostDto;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EventHubWriteService {

    private final EventHubClient eventHubWriteClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventHubWriteService(@Qualifier("eventHubWriteClient") EventHubClient eventHubWriteClient, ObjectMapper objectMapper) {
        this.eventHubWriteClient = eventHubWriteClient;
        this.objectMapper = objectMapper;
    }

    public void send(List<PostDto> posts) {
        List<EventData> events = posts.stream()
            .flatMap(this::serialize)
            .map(this::createEvent)
            .collect(toList());
        if (events.isEmpty()) {
            throw new RuntimeException("Failed to serialize all posts");
        }
        eventHubWriteClient.send(events, "1")
            .whenComplete((returnValue, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to send events", throwable);
                    throw new RuntimeException(throwable);
                } else {
                    log.info("Successfully sent {} events (based on {} posts)", events.size(), posts.size());
                }
            });
    }

    private EventData createEvent(byte[] data) {
        EventData eventData = new EventData(data);
        eventData.getProperties().put("serialVersionUID", PostDto.serialVersionUID);
        return eventData;
    }

    private Stream<byte[]> serialize(PostDto post) {
        try {
            return Stream.of(objectMapper.writeValueAsBytes(post));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize post: " + post.getId(), e);
            return Stream.empty();
        }
    }
}
