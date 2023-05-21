package com.microservice.eventticketingservice.service;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.EventDTOAdapter;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import com.microservice.clients.eventAnalytics.EventAnalysisClient;
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
    private final EventAnalysisClient eventAnalysisClient;
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
        try {
            Event event = eventClient.getEvent(ticketRequest.getEventId()).getBody();
            for (TicketItems ticketItems : event.getTicketItemsList()) {
                if (ticketItems.getLabel().equals(ticketRequest.getTicketItem())) {
                    if (ticketItems.getAvailableQuantity() <= 0) {
                        log.error("ticketsItem not available");
                        throw new Exception("no more avalible tickets");
                    } else {
                        ticketItems.setAvailableQuantity(ticketItems.getAvailableQuantity() - 1);
                        eventClient.updateEvent(EventDTOAdapter.mapEventToEventRequest(event),event.getId());


                        ticketRequest.setPrice(ticketItems.getPrice());
                    }
                }
            }
            Ticket ticket = TicketDTOAdapter.getTicket(ticketRequest);
            ticket.setStatus(ACTIVE);
            ticket.setEvent(event);


            //todo
            // notification event
            // call payment service

            ticketRepository.save(ticket);

            //create a function to generate ticket file
            log.info("ticket created successfully");

            return ticket;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;

        }

    }

    public void ticketRefund(String ticketId){
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        if(byId.isPresent()){
            Ticket ticket = byId.get();
           for(TicketItems ticketItems: ticket.getEvent().getTicketItemsList()){
               if (ticket.getTicketItem().equals(ticketItems.getLabel())){
                   ticketItems.setAvailableQuantity(ticketItems.getAvailableQuantity()+1);
                   eventClient.updateEvent(EventDTOAdapter.mapEventToEventRequest(ticket.getEvent()),ticket.getEvent().getId());
               }
           }
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

