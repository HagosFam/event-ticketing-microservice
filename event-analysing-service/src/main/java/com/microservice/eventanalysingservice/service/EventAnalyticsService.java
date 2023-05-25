package com.microservice.eventanalysingservice.service;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import com.microservice.clients.ticket.TicketClient;
import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.eventanalysingservice.models.EventAnalytics;
import com.microservice.eventanalysingservice.repository.EventAnalysisRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventAnalyticsService {
    private final EventAnalysisRepository eventAnalysisRepository;
    private final EventClient eventClient;
    private final TicketClient ticketClient;
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackEvent")
    @Bulkhead(name = "bulkheadEventAnalysisService", fallbackMethod = "fallbackEvent" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventAnalysisService", fallbackMethod = "fallbackEvent")
    public Event getEvent(String id){
        Event event = eventClient.getEvent(id).getBody();
        return event;
    }


    @CircuitBreaker(name = "eventAnalysisService", fallbackMethod = "fallbackEventAnalysis")
    @Bulkhead(name = "bulkheadEventAnalysisService", fallbackMethod = "fallbackEventAnalysis" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventAnalysisService", fallbackMethod = "fallbackEventAnalysis")
    public EventAnalytics getEventAnalyticsDataForEvent(String eventId){
        log.info("fetching event analytics for event id:{}", eventId);
        Event event = getEvent(eventId);
        Optional<EventAnalytics> eventAnalytics = eventAnalysisRepository.findByEventId(event.getId());
        if(eventAnalytics.isPresent()){
            EventAnalytics eventAnalytics1 = eventAnalytics.get();
            return eventAnalytics1;
        }
        else {
            log.error("there exist no event class with such id : {}", eventId);
            return null;
        }
    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackEventAnalysis")
    @Bulkhead(name = "bulkheadEventAnalysisService", fallbackMethod = "fallbackEventAnalysis" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventAnalysisService", fallbackMethod = "fallbackEventAnalysis")
    public  EventAnalytics createEventAnalyticsForEvent(String eventId){
        log.info("creating event analytics class for event id: {}", eventId);
        Event event = getEvent(eventId);
        EventAnalytics eventAnalytics = new EventAnalytics()
                .builder().event(event)
                .totalRevenue(BigDecimal.ZERO)
                .totalNumberOfTickets(event.getTicketItemsList()
                        .stream()
                        .map(TicketItems::getAvailableQuantity)
                        .reduce(0L, Long::sum)
                ).build();
        eventAnalysisRepository.save(eventAnalytics);
        return  eventAnalytics;


    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackEventAnalysis")
    @Bulkhead(name = "bulkheadEventAnalysisService", fallbackMethod = "fallbackEventAnalysis" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventAnalysisService", fallbackMethod = "fallbackEventAnalysis")
    public EventAnalytics addTicketSales(String ticketId){
        log.info("Adding ticket sales to eventAnalytics for event id: {}",ticketId);
        Ticket ticket= ticketClient.getTicketById(ticketId).getBody();
        EventAnalytics eventAnalyticsDataForEvent = getEventAnalyticsDataForEvent(ticket.getEvent().getId());
        eventAnalyticsDataForEvent.addProcessedTickets(ticket.getId());
        addTicketSalesRevenue(eventAnalyticsDataForEvent,ticket.getPrice());

        eventAnalysisRepository.save(eventAnalyticsDataForEvent);
        return eventAnalyticsDataForEvent;

    }

    private BigDecimal addTicketSalesRevenue(EventAnalytics eventAnalytics,BigDecimal price) {
        eventAnalytics.addRevenue(price);
        return price;

    }
    @CircuitBreaker(name = "eventService", fallbackMethod = "fallbackEventAnalysis")
    @Bulkhead(name = "bulkheadEventAnalysisService", fallbackMethod = "fallbackEventAnalysis" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryEventAnalysisService", fallbackMethod = "fallbackEventAnalysis")

    public EventAnalytics refundedTickets(String ticketId){
        Ticket ticketById = ticketClient.getTicketById(ticketId).getBody();
        EventAnalytics eventAnalyticsDataForEvent = getEventAnalyticsDataForEvent(ticketById.getEvent().getId());
        addTicketSalesRevenue(eventAnalyticsDataForEvent,ticketById.getPrice().multiply(BigDecimal.valueOf(-1)));
        eventAnalysisRepository.save(eventAnalyticsDataForEvent);
        return eventAnalyticsDataForEvent;

    }
    public Event fallbackEvent(String id, Throwable throwable) {
        Event fallbackEvent = new Event();
        fallbackEvent.setId(id);
        fallbackEvent.setDescription("event not available try again");
        return fallbackEvent;
    }
    public Ticket fallbackTicket(String ticketId,Throwable t) {
        Ticket ticket = new Ticket();
        ticket.setId("N/A");
        ticket.setTicketNumber("SERVICE NOT AVAILABLE");
        ticket.setTicketItem("SERVICE NOT AVAILABLE");

        return ticket;
    }
    public EventAnalytics fallbackEventAnalysis(String eventId,Throwable t){
        return new EventAnalytics()
                .builder()
                .id("Not Availabe")
                .build();

    }


}
