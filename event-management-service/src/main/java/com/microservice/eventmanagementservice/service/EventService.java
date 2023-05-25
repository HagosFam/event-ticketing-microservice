package com.microservice.eventmanagementservice.service;

import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.clients.eventAnalytics.dto.EventAnalytics;
import com.microservice.clients.user.UserClient;
import com.microservice.clients.user.dtos.User;
import com.microservice.eventmanagementservice.messaging.publisher.EventPublisher;
import com.microservice.eventmanagementservice.models.Event;
import com.microservice.eventmanagementservice.repository.EventRepository;
import com.microservice.eventmanagementservice.service.dtos.EventDTOAdapter;
import com.microservice.eventmanagementservice.service.dtos.EventRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final EventAnalysisClient eventAnalysisClient;
    private final UserClient userClient;
    private final EventPublisher eventPublisher;
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    @Bulkhead(name = "bulkheadEventService", fallbackMethod = "fallbackUser" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventService", fallbackMethod = "fallbackUser")
    public User getUser(long userId){
        return userClient.getAUser(userId).getBody();

    }

    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackCreateEvent")
    @Bulkhead(name = "bulkheadEventService", fallbackMethod = "fallbackCreateEvent" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventService", fallbackMethod = "fallbackCreateEvent")
    public Event createEvent(EventRequest eventRequest){
        log.info("inside user");

        User user = getUser(eventRequest.getHostId());
        log.info(user.toString());
        Event event= EventDTOAdapter.getEvent(eventRequest);

        event.setHost(user);
        eventRepository.save(event);
        eventPublisher.publish(event);
        log.info("event successfully created");
        return event;

    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackEvent")
    @Bulkhead(name = "bulkheadEventService", fallbackMethod = "fallbackEvent" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventService", fallbackMethod = "fallbackEvent")
    public Event getEvent(String eventId){
        log.info("fetching an event with an id:{}", eventId);
        Optional<Event> eventByid = eventRepository.findById(eventId);
        if(eventByid.isPresent()){
            log.info("event id:{} fetched successfully", eventId);
            return eventByid.get();
        }
        else {
            log.error("no event exists with id:{}", eventId);
            return null;
        }

    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackHostListEvent")
    @Bulkhead(name = "bulkheadEventService", fallbackMethod = "fallbackHostListEvent" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventService", fallbackMethod = "fallbackHostListEvent")
    public List<Event> getEventsByhost(long hostId){
        log.info("fetching list of events for host with an id:{}", hostId);
        List<Event> eventById = eventRepository.findByHostId(hostId);
        return eventById;

    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackEvent")
    @Bulkhead(name = "bulkheadEventService", fallbackMethod = "fallbackEvent" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventService", fallbackMethod = "fallbackEvent")
    public Event UpdateEvent(EventRequest eventRequest , String id){
        Event event = EventDTOAdapter.getEvent(eventRequest);
        event.setId(id);
        log.info("event with id: {} updated successfully", event.getId());
        eventRepository.save(event);
        return event;
    }

    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackListEvent")
    @Bulkhead(name = "bulkheadEventService", fallbackMethod = "fallbackListEvent" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventService", fallbackMethod = "fallbackListEvent")
    public List<Event> getAllEvents() {
        log.info("returning list of all events");
        return eventRepository.findAll();

    }
    public User fallbackUser(long id, Throwable throwable) {
        User fallbackUser = new User();
        fallbackUser.setId(id);
        fallbackUser.setUsername("N/A");
        fallbackUser.setEmail("N/A");
        fallbackUser.setDeleted(true);
        // Set other minimal data as needed

        return fallbackUser;
    }
    public Event fallbackEvent(String id, Throwable throwable) {
       Event fallbackEvent = new Event();
        fallbackEvent.setId(id);
        fallbackEvent.setDescription("event not available try again");
        return fallbackEvent;
    }
    public Event fallbackCreateEvent(EventRequest eventRequest, Throwable throwable) {
        Event fallbackEvent = new Event();
        fallbackEvent.setDescription("event not available try again");
        log.info(throwable.getMessage());
        return fallbackEvent;
    }
    public List<Event> fallbackListEvent( Throwable throwable) {
        Event fallbackEvent = new Event();
        fallbackEvent.setDescription("event not available try again");
        List<Event> fallbackListEvent= new ArrayList<>();
        fallbackListEvent.add(fallbackEvent);
        return fallbackListEvent;
    }
    public List<Event> fallbackHostListEvent(long hostid,Throwable throwable) {
        Event fallbackEvent = new Event();
        fallbackEvent.setDescription("event not available try again");
        List<Event> fallbackListEvent= new ArrayList<>();
        fallbackListEvent.add(fallbackEvent);
        return fallbackListEvent;
    }
}
