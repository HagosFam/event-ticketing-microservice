package com.microservice.eventmanagementservice;

import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.clients.user.UserClient;
import com.microservice.clients.user.dtos.User;
import com.microservice.eventmanagementservice.messaging.publisher.EventPublisher;
import com.microservice.eventmanagementservice.models.Event;
import com.microservice.eventmanagementservice.repository.EventRepository;
import com.microservice.eventmanagementservice.service.EventService;
import com.microservice.eventmanagementservice.service.dtos.EventRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventAnalysisClient eventAnalysisClient;
    @Mock
    private UserClient userClient;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private CircuitBreaker circuitBreaker;
    @Mock
    private Retry retry;

    @InjectMocks
    private EventService eventService;

    PodamFactory podamFactory= new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUser_ShouldReturnUser() {
        long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("JohnDoe");
        expectedUser.setEmail("john.doe@example.com");

        when(userClient.getAUser(userId)).thenReturn(ResponseEntity.ok(expectedUser));

        User actualUser = eventService.getUser(userId);

        assertEquals(expectedUser, actualUser);
        verify(userClient, times(1)).getAUser(userId);
    }

    @Test
    void createEvent_ShouldReturnCreatedEvent() {
        long hostId = 1L;
        EventRequest eventRequest = new EventRequest();
        eventRequest.setHostId(hostId);

        User expectedUser = new User();
        expectedUser.setId(hostId);
        expectedUser.setUsername("JohnDoe");
        expectedUser.setEmail("john.doe@example.com");

        Event expectedEvent = new Event();
        expectedEvent.setHost(expectedUser);

        when(userClient.getAUser(hostId)).thenReturn(ResponseEntity.ok(expectedUser));
        when(eventRepository.save(any(Event.class))).thenReturn(expectedEvent);

        Event actualEvent = eventService.createEvent(eventRequest);

        assertEquals(expectedEvent.getName(), actualEvent.getName());
        verify(userClient, times(1)).getAUser(hostId);
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(eventPublisher, times(1)).publish(any(Event.class));
    }

    @Test
    void getEvent_ExistingEventId_ShouldReturnEvent() {
        String eventId = "event-123";
        Event expectedEvent = new Event();
        expectedEvent.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(expectedEvent));

        Event actualEvent = eventService.getEvent(eventId);

        assertEquals(expectedEvent, actualEvent);
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void getEvent_NonExistingEventId_ShouldReturnNull() {
        String eventId = "event-123";

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Event actualEvent = eventService.getEvent(eventId);

        assertEquals(null, actualEvent);
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void getEventsByHost_ShouldReturnEventList() {
        long hostId = 1L;
        User expectedUser = new User();
        expectedUser.setId(hostId);
        Event event = new Event();
        event.setHost(expectedUser);
        List<Event> expectedEventList = Collections.singletonList(event);

        when(eventRepository.findByHostId(hostId)).thenReturn(expectedEventList);

        List<Event> actualEventList = eventService.getEventsByhost(hostId);

        assertEquals(expectedEventList, actualEventList);
        verify(eventRepository, times(1)).findByHostId(hostId);
    }



    @Test
    void getAllEvents_ShouldReturnEventList() {
        List<Event> expectedEventList = Collections.singletonList(new Event());

        when(eventRepository.findAll()).thenReturn(expectedEventList);

        List<Event> actualEventList = eventService.getAllEvents();

        assertEquals(expectedEventList, actualEventList);
        verify(eventRepository, times(1)).findAll();
    }
}
