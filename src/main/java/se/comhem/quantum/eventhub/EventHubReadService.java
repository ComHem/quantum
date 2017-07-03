package se.comhem.quantum.eventhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubPartitionRuntimeInformation;
import com.microsoft.azure.eventhubs.PartitionReceiver;
import com.microsoft.azure.servicebus.ServiceBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.comhem.quantum.feed.PostDto;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EventHubReadService {

    private final EventHubClient eventHubReadClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventHubReadService(@Qualifier("eventHubReadClient") EventHubClient eventHubReadClient, ObjectMapper objectMapper) throws ServiceBusException {
        this.eventHubReadClient = eventHubReadClient;
        this.objectMapper = objectMapper;
    }

    public List<PostDto> read() {
        PartitionReceiver receiver = null;
        try {
            receiver = eventHubReadClient.getPartitionRuntimeInformation("1")
                .thenApply(this::calculateStartingOffset)
                .thenCompose(this::createReceiverForOffset)
                .get();
            receiver.setReceiveTimeout(Duration.ofSeconds(1));
            receiver.setPrefetchCount(999);
            return receive(receiver);
        } catch (ServiceBusException | InterruptedException | ExecutionException e) {
            log.error("Failed to configure receiver: " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (receiver != null) {
                receiver.close();
            }
        }
    }

    private String calculateStartingOffset(EventHubPartitionRuntimeInformation partitionInfo) {
        return String.valueOf(Long.valueOf(partitionInfo.getLastEnqueuedOffset())-50000L);
    }

    private CompletionStage<PartitionReceiver> createReceiverForOffset(String offset) {
        try {
            return eventHubReadClient.createReceiver(EventHubClient.DEFAULT_CONSUMER_GROUP_NAME, "1", offset, true);
        } catch (ServiceBusException e) {
            log.error("Failed to create receiver: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private List<PostDto> receive(PartitionReceiver receiver) throws ServiceBusException {
        try {
            List<EventData> allEvents = getLatestEvents(receiver);
            return allEvents.stream()
                .skip(Math.max(0, allEvents.size() - 100))
                .filter(this::compatibleSerializedVersion)
                .flatMap(this::deserialize)
                .collect(collectingAndThen(toList(), Lists::reverse));
        } catch (ServiceBusException e) {
            log.error("Failed to receive events", e);
            throw new RuntimeException(e);
        }
    }

    private List<EventData> getLatestEvents(PartitionReceiver receiver) throws ServiceBusException {
        Long start = System.currentTimeMillis();
        List<EventData> allEvents = new ArrayList<>(100);
        while (true) {
            Iterable<EventData> events = receiver.receiveSync(100);
            if (events == null) break;
            events.forEach(allEvents::add);
        }
        if (allEvents.isEmpty()) {
            log.error("Failed get any events");
            throw new RuntimeException("Failed get any events");
        }
        log.info("Took {} ms to fetch {} events", System.currentTimeMillis() - start, allEvents.size());
        return allEvents;
    }

    private boolean compatibleSerializedVersion(EventData eventData) {
        return PostDto.serialVersionUID.equals(eventData.getProperties().get("serialVersionUID"));
    }

    private Stream<PostDto> deserialize(EventData event) {
        try {
            return Stream.of(objectMapper.readValue(event.getBytes(), PostDto.class));
        } catch (java.io.IOException e) {
            log.error("Failed to deserialize post: " + event, e);
            return Stream.empty();
        }
    }
}
