package com.microservice.eventanalysingservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.clients.messagingObjects.EventData;
import com.microservice.clients.messagingObjects.Operation;
import com.microservice.clients.messagingObjects.OrderData;
import com.microservice.eventanalysingservice.service.EventAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener {
    private final EventAnalyticsService eventAnalyticsService;
    @Value("${event-service.event-topic-name}")
    private String TopicName;
    @KafkaListener(id = "${event-service.group-id-event}",topics = "${event-service.event-topic-name}")
    public void receiveEvent(String message) throws JsonProcessingException {
        log.info("Event creation event recieved");
        ObjectMapper objectMapper= new ObjectMapper();
        EventData eventData = objectMapper.readValue(message, EventData.class);
        log.info("successfully acquired message: {}", message);
        eventAnalyticsService.createEventAnalyticsForEvent(eventData.getId());
    }
    @KafkaListener(id ="${event-service.group-id-order}", topics ="${event-service.order-topic-name}" )
    public void recieveTicketSale(String message) throws JsonProcessingException {
        log.info("Order creation event recieved");
        ObjectMapper objectMapper= new ObjectMapper();
        OrderData orderData = objectMapper.readValue(message, OrderData.class);
        log.info("successfully acquired message: {}", message);
        if(orderData.getOperation().equals(Operation.BOUGHT)){
        for(String ticket: orderData.getTicketId()){
            eventAnalyticsService.addTicketSales(ticket);
        }

        }else if(orderData.getOperation().equals(Operation.REFUNDED)){
            for(String ticket: orderData.getTicketId()){
                eventAnalyticsService.refundedTickets(ticket);
            }

        }
    }
}
