package com.microservice.eventanalysingservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.clients.messagingObjects.EventData;
import com.microservice.eventanalysingservice.service.EventAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventCreatedMessageListener {
    private final EventAnalyticsService eventAnalyticsService;
    @Value("${event-service.event-topic-name}")
    private String TopicName;
    @KafkaListener(id = "${event-service.group-id}",topics = "${event-service.event-topic-name}")
    public void receive(String message) throws JsonProcessingException {
        log.info("event response received with  partitions");
        ObjectMapper objectMapper= new ObjectMapper();
        EventData eventData = objectMapper.readValue(message, EventData.class);
        log.info("successfully acquired message: {}", message);
        eventAnalyticsService.createEventAnalyticsForEvent(eventData.getId());
    }
}
