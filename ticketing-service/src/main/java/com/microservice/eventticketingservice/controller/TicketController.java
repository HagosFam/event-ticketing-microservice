package com.microservice.eventticketingservice.controller;

import com.microservice.eventticketingservice.models.Ticket;
import com.microservice.eventticketingservice.service.TicketService;
import com.microservice.eventticketingservice.service.dtos.TicketRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable String ticketId) {
        Ticket ticket = ticketService.getTicketFromId(ticketId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/buy")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket ticket = ticketService.createATicket(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @PostMapping("/refund/{ticketId}")
    public ResponseEntity<Void> refundTicket(@PathVariable String ticketId) {
        ticketService.ticketRefund(ticketId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scan/{ticketId}")
    public ResponseEntity<Ticket> scanTicket(@PathVariable String ticketId) {
        Ticket ticket = ticketService.ticketScan(ticketId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
