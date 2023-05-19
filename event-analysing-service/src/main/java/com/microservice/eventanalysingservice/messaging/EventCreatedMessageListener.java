package com.microservice.eventanalysingservice.messaging;

import com.microservice.kafka.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import schema_models.EventData;

import java.util.List;

@Component
@Slf4j
public class EventCreatedMessageListener implements KafkaConsumer<EventData> {
    @Override
    @KafkaListener(id ="${kafka-consumer-config.event-consumer-group-id}",topics = "${event-service.event-response-topic-name}")
    public void receive(@Payload List<EventData> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of events response received wiht keys:{}, partitions:{}, and offsets: {}",
               messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());
        messages.forEach(System.out::println);
    }
}
