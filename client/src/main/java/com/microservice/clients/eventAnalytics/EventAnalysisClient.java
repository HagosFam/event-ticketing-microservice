package com.microservice.clients.eventAnalytics;

import com.microservice.clients.eventAnalytics.dto.EventAnalytics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "eventAnalysis", url = "${clients.eventAnalysis.url}")
public interface EventAnalysisClient {
    @GetMapping("/eventAnalytics/{eventId}")
    ResponseEntity<EventAnalytics> getEventAnalyticsDataForEvent(@PathVariable("eventId") String eventId);

    @PostMapping("/eventAnalytics/{eventId}")
    ResponseEntity<EventAnalytics> createEventAnalyticsForEvent(@PathVariable("eventId") String eventId);

    @PostMapping("/eventAnalytics/ticket-sales/{ticketId}")
    ResponseEntity<EventAnalytics> addTicketSales(@PathVariable("ticketId") String ticketId);

    @PostMapping("/eventAnalytics/refunded-tickets/{ticketId}")
    ResponseEntity<EventAnalytics> refundedTickets(@PathVariable("ticketId") String ticketId);

    }



