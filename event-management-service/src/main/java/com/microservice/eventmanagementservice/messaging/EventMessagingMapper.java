package com.microservice.eventmanagementservice.messaging;

import com.microservice.eventmanagementservice.models.Event;
import org.springframework.stereotype.Component;
import schema_models.EventData;
@Component
public class EventMessagingMapper {

    public EventData getEventData(Event event){
        return EventData.newBuilder()
                .setMessage(event.getDescription())
                .build();
    }

}
