package com.microservice.clients.eventAnalytics;

import com.microservice.clients.eventAnalytics.dto.EventAnalytics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("eventAnalysis")
public interface EventAnalysisClient {
    @GetMapping("/event-analytics/{eventId}")
    ResponseEntity<EventAnalytics> getEventAnalyticsDataForEvent(@PathVariable("eventId") String eventId);

    @PostMapping("/event-analytics/{eventId}")
    ResponseEntity<EventAnalytics> createEventAnalyticsForEvent(@PathVariable("eventId") String eventId);

    @PostMapping("/event-analytics/ticket-sales/{ticketId}")
    ResponseEntity<EventAnalytics> addTicketSales(@PathVariable("ticketId") String ticketId);

    @PostMapping("/event-analytics/refunded-tickets/{ticketId}")
    ResponseEntity<EventAnalytics> refundedTickets(@PathVariable("ticketId") String ticketId);

    }



