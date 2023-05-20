package com.microservice.eventanalysingservice.service;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import com.microservice.clients.ticket.TicketClient;
import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.eventanalysingservice.models.EventAnalytics;
import com.microservice.eventanalysingservice.repository.EventAnalysisRepository;
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
    public EventAnalytics getEventAnalyticsDataForEvent(String eventId){
        log.info("fetching event analytics for event id:{}", eventId);
        Event event = eventClient.getEvent(eventId).getBody();
        Optional<EventAnalytics> eventAnalytics = eventAnalysisRepository.findByEvent(event);
        if(eventAnalytics.isPresent()){
            EventAnalytics eventAnalytics1 = eventAnalytics.get();
            return eventAnalytics1;
        }
        else {
            log.error("there exist no event class with such id : {}", eventId);
            return null;
        }
    }
    public  EventAnalytics createEventAnalyticsForEvent(String eventId){
        log.info("creating event analytics class for event id: {}", eventId);
        Event event = eventClient.getEvent(eventId).getBody();
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

    public EventAnalytics refundedTickets(String ticketId){
        Ticket ticketById = ticketClient.getTicketById(ticketId).getBody();
        EventAnalytics eventAnalyticsDataForEvent = getEventAnalyticsDataForEvent(ticketById.getEvent().getId());
        eventAnalyticsDataForEvent.addProcessedTickets(ticketById.getId());
        addTicketSalesRevenue(eventAnalyticsDataForEvent,ticketById.getPrice().multiply(BigDecimal.valueOf(-1)));
        eventAnalysisRepository.save(eventAnalyticsDataForEvent);
        return eventAnalyticsDataForEvent;

    }


}
