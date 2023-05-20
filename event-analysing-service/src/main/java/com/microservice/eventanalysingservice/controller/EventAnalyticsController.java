package com.microservice.eventanalysingservice.controller;

import com.microservice.eventanalysingservice.models.EventAnalytics;
import com.microservice.eventanalysingservice.service.EventAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event-analytics")
@RequiredArgsConstructor
public class EventAnalyticsController {
    private final EventAnalyticsService eventAnalyticsService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventAnalytics> getEventAnalyticsDataForEvent(@PathVariable String eventId) {
        EventAnalytics eventAnalytics = eventAnalyticsService.getEventAnalyticsDataForEvent(eventId);
        if (eventAnalytics != null) {
            return ResponseEntity.ok(eventAnalytics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<EventAnalytics> createEventAnalyticsForEvent(@PathVariable String eventId) {
        EventAnalytics eventAnalytics = eventAnalyticsService.createEventAnalyticsForEvent(eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventAnalytics);
    }

    @PostMapping("/ticket-sales/{ticketId}")
    public ResponseEntity<EventAnalytics> addTicketSales(@PathVariable String ticketId) {
        EventAnalytics eventAnalytics = eventAnalyticsService.addTicketSales(ticketId);
        return ResponseEntity.ok(eventAnalytics);
    }

    @PostMapping("/refunded-tickets/{ticketId}")
    public ResponseEntity<EventAnalytics> refundedTickets(@PathVariable String ticketId) {
        EventAnalytics eventAnalytics = eventAnalyticsService.refundedTickets(ticketId);
        return ResponseEntity.ok(eventAnalytics);
    }
}
