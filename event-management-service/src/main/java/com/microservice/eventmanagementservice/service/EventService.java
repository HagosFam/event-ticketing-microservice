package com.microservice.eventmanagementservice.service;

import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.eventmanagementservice.models.Event;
import com.microservice.eventmanagementservice.repository.EventRepository;
import com.microservice.eventmanagementservice.service.dtos.EventDTOAdapter;
import com.microservice.eventmanagementservice.service.dtos.EventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final EventAnalysisClient eventAnalysisClient;

    public Event createEvent(EventRequest eventRequest){
        Event event= EventDTOAdapter.getEvent(eventRequest);
        eventRepository.save(event);
        eventAnalysisClient.createEventAnalyticsForEvent(event.getId());
        log.info("event successfully created");
        return event;

    }

    public Event getEvent(String eventId){
        Optional<Event> eventByid = eventRepository.findById(eventId);
        if(eventByid.isPresent()){
            return eventByid.get();
        }
        else {
            return null;
        }

    }
    public List<Event> getEventsByhost(long hostId){
        List<Event> eventById = eventRepository.findByHostId(hostId);
        return eventById;

    }
    public Event UpdateEvent(EventRequest eventRequest , String id){
        Event event = EventDTOAdapter.getEvent(eventRequest);
        event.setId(id);
        eventRepository.save(event);
        return event;
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
