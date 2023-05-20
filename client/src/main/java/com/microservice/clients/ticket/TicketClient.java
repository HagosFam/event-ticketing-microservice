package com.microservice.clients.ticket;

import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.clients.ticket.dtos.TicketRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ticket-service", url = "http://localhost:8081")
public interface TicketClient {

    @PostMapping("/buy")
    ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest);
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable String ticketId);

    @PostMapping("/refund/{ticketId}")
    ResponseEntity<Void> refundTicket(@PathVariable("ticketId") String ticketId);

    @DeleteMapping("/{ticketId}")
    ResponseEntity<Void> deleteTicket(@PathVariable("ticketId") String ticketId);

    @PostMapping("/scan/{ticketId}")
    ResponseEntity<Ticket> scanTicket(@PathVariable("ticketId") String ticketId);
}