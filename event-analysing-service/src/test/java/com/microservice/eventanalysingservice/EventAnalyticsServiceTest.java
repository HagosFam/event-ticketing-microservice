package com.microservice.eventanalysingservice;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.ticket.TicketClient;
import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.eventanalysingservice.models.EventAnalytics;
import com.microservice.eventanalysingservice.repository.EventAnalysisRepository;
import com.microservice.eventanalysingservice.service.EventAnalyticsService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EventAnalyticsServiceTest {
    @Mock
    private EventAnalysisRepository eventAnalysisRepository;
    @Mock
    private EventClient eventClient;
    @Mock
    private TicketClient ticketClient;

    @InjectMocks
    private EventAnalyticsService eventAnalyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEvent_ValidEventId_ShouldReturnEvent() {
        String eventId = "event-123";
        Event expectedEvent = new Event();
        expectedEvent.setId(eventId);

        when(eventClient.getEvent(eventId)).thenReturn(ResponseEntity.ok(expectedEvent));

        Event actualEvent = eventAnalyticsService.getEvent(eventId);

        assertEquals(expectedEvent, actualEvent);
        verify(eventClient, times(1)).getEvent(eventId);
    }



    @Test
    void getEventAnalyticsDataForEvent_ExistingEventId_ShouldReturnEventAnalytics() {
        String eventId = "event-123";
        Event expectedEvent = new Event();
        expectedEvent.setId(eventId);

        EventAnalytics expectedEventAnalytics = new EventAnalytics();
        expectedEventAnalytics.setEvent(expectedEvent);

        when(eventAnalysisRepository.findByEventId(eventId)).thenReturn(Optional.of(expectedEventAnalytics));
        when(eventClient.getEvent(eventId)).thenReturn(ResponseEntity.ok(expectedEvent));

        EventAnalytics actualEventAnalytics = eventAnalyticsService.getEventAnalyticsDataForEvent(eventId);

        assertEquals(expectedEventAnalytics, actualEventAnalytics);
        verify(eventAnalysisRepository, times(1)).findByEventId(eventId);
        verify(eventClient, times(1)).getEvent(eventId);
    }





}

