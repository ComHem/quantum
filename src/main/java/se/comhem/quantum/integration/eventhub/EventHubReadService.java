package se.comhem.quantum.integration.eventhub;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.comhem.quantum.model.Post;

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

    private static long ESTIMATE_MESSAGE_SIZE = 2000L;
    private static int NUMBER_OF_MESSAGES_TO_READ = 100;


    private final EventHubClient eventHubReadClient;
    private final String partition;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventHubReadService(@Qualifier("eventHubReadClient") EventHubClient eventHubReadClient,
                               @Value("${quantum.eventhub.partition}") String partition,
                               ObjectMapper objectMapper) throws ServiceBusException {
        this.eventHubReadClient = eventHubReadClient;
        this.partition = partition;
        this.objectMapper = objectMapper;
    }

    public List<Post> read() {
        PartitionReceiver receiver = null;
        try {
            receiver = eventHubReadClient.getPartitionRuntimeInformation(partition)
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
        long offset = Long.valueOf(partitionInfo.getLastEnqueuedOffset()) - NUMBER_OF_MESSAGES_TO_READ * ESTIMATE_MESSAGE_SIZE;
        return String.valueOf(offset);
    }

    private CompletionStage<PartitionReceiver> createReceiverForOffset(String offset) {
        try {
            return eventHubReadClient.createReceiver(EventHubClient.DEFAULT_CONSUMER_GROUP_NAME, partition, offset, true);
        } catch (ServiceBusException e) {
            log.error("Failed to create receiver: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private List<Post> receive(PartitionReceiver receiver) throws ServiceBusException {
        try {
            Long start = System.currentTimeMillis();
            List<EventData> events = getLatestEvents(receiver);
            List<Post> posts = events.stream()
                .skip(Math.max(0, events.size() - 100))
                .filter(this::compatibleSerializedVersion)
                .flatMap(this::deserialize)
                .distinct()
                .collect(collectingAndThen(toList(), Lists::reverse));
            log.info("Took {} ms to fetch {} events (after filtering {} posts)", System.currentTimeMillis() - start, events.size(), posts.size());
            return posts;
        } catch (ServiceBusException e) {
            log.error("Failed to receive events", e);
            throw new RuntimeException(e);
        }
    }

    private List<EventData> getLatestEvents(PartitionReceiver receiver) throws ServiceBusException {
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
        return allEvents;
    }

    private boolean compatibleSerializedVersion(EventData eventData) {
        return Post.serialVersionUID.equals(eventData.getProperties().get("serialVersionUID"));
    }

    private Stream<Post> deserialize(EventData event) {
        try {
            return Stream.of(objectMapper.readValue(event.getBytes(), Post.class));
        } catch (java.io.IOException e) {
            log.error("Failed to deserialize post: " + event, e);
            return Stream.empty();
        }
    }
}
