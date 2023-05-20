package com.microservice.clients.event;

import com.microservice.clients.event.dtos.EventRequest;
import com.microservice.clients.event.dtos.valueObjects.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "event",
                url = "http://localhost:8083")
public interface EventClient {
    @PostMapping("/api/events")
    ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest);

    @GetMapping("/api/events/{eventId}")
    ResponseEntity<Event> getEvent(@PathVariable("eventId") String eventId);

    @GetMapping("/api/events/host/{hostId}")
    ResponseEntity<List<Event>> getEventsByHost(@PathVariable("hostId") long hostId);

    @PutMapping("/api/events/{eventId}")
    ResponseEntity<Event> updateEvent(@RequestBody EventRequest eventRequest, @PathVariable("eventId") String eventId);

}
