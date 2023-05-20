package com.microservice.eventanalysingservice.service;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import com.microservice.eventanalysingservice.models.EventAnalytics;
import com.microservice.eventanalysingservice.repository.EventAnalysisRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventAnalyticsService {
    private final EventAnalysisRepository eventAnalysisRepository;
    private final EventClient eventClient;
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
                .totalTicketsRefunded(0)
                .totalRevenue(BigDecimal.ZERO)
                .totalTicketsSold(0)
                .totalNumberOfTicket(event.getTicketItemsList()
                        .stream()
                        .map(TicketItems::getAvailableQuantity)
                        .reduce(0L, Long::sum)
                ).build();
        return  eventAnalytics;


    }


}
