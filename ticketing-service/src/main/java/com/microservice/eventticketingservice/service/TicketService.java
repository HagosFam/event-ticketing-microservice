package com.microservice.eventticketingservice.service;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.eventticketingservice.models.Ticket;
import com.microservice.eventticketingservice.repository.TicketRepository;
import com.microservice.eventticketingservice.service.dtos.TicketRequest;
import com.microservice.eventticketingservice.service.dtos.TicketDTOAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.microservice.eventticketingservice.models.enums.TicketStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventClient eventClient;
    public Ticket getTicketFromId(String ticketId){
        Optional<Ticket> ticketbyId = ticketRepository.findById(ticketId);
        if(ticketbyId.isPresent()){
            Ticket ticket = ticketbyId.get();
            return ticket;

        }
        else {
            log.error("ticket with an id: {} was not found", ticketId);
            return null;
        }
    }

    public Ticket createATicket(TicketRequest ticketRequest){
        Ticket ticket= TicketDTOAdapter.getTicket(ticketRequest);
        ticket.setStatus(ACTIVE);
        ResponseEntity<Event> event = eventClient.getEvent(ticketRequest.getEventId());
        ticket.setEvent(event.getBody());

        //todo
        // notification event
        // check if there are any available tickets
        // get ticket entry details for price
        // decrement available quantity by 1
        // call payment service

        ticketRepository.save(ticket);
        //create a function to generate ticket file
        log.info("ticket created successfully");

        return ticket;

    }

    public void ticketRefund(String ticketId){
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        if(byId.isPresent()){
            Ticket ticket = byId.get();
            ticket.setStatus(CANCELLED);
            ticketRepository.save(ticket);
            // make a request to payment method inorder to handle actual money refund
            log.info("ticket with id :{} was successfully refunded", ticketId);
        }
    }
    public void deleteTicket(String ticketId){
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        if (byId.isPresent()){
            Ticket ticket = byId.get();
            ticket.setDeleted(true);
            // increment available quantity by 1
            // ticket cancelled event is published

            log.info("ticket is cancelled successfully");

        }
        else {
            log.error("this ticket is not valid");
        }
    }
    public Ticket ticketScan(String ticketId){
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        if(byId.isPresent()){
            log.info("Ticket scanned successfully");

            //publish ticket scanned event
            Ticket ticket = byId.get();
            ticket.setStatus(USED);
            ticketRepository.save(ticket);
            return ticket;
        }

        else {
            log.error("please use a valid ticket");
            return null;
        }


    }

}

