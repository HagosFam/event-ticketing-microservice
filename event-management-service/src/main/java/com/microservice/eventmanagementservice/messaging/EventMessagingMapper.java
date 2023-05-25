package com.microservice.eventmanagementservice.messaging;

import com.microservice.clients.messagingObjects.EventData;
import com.microservice.eventmanagementservice.models.Event;
import org.springframework.stereotype.Component;
@Component
public class EventMessagingMapper {

    public EventData getEventData(Event event){
        return EventData.builder()
                .id(event.getId())
                .eventType(event.getEventType())
                .ageRestriction(event.getAgeRestriction())
                .location(event.getLocation())
                .host(event.getHost().getId())
                .name(event.getName())
                .description(event.getDescription())
                .build();
    }

}
