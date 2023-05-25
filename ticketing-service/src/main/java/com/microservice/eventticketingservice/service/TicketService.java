package com.microservice.eventticketingservice.service;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.EventDTOAdapter;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.clients.user.UserClient;
import com.microservice.clients.user.dtos.User;
import com.microservice.eventticketingservice.models.Ticket;
import com.microservice.eventticketingservice.repository.TicketRepository;
import com.microservice.eventticketingservice.service.dtos.TicketRequest;
import com.microservice.eventticketingservice.service.dtos.TicketDTOAdapter;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.microservice.eventticketingservice.models.enums.TicketStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    private final UserClient userClient;
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    @Bulkhead(name = "bulkheadTicketService", fallbackMethod = "fallbackUser" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService", fallbackMethod = "fallbackUser")
    public User getUser(long userId){
        User user = userClient.getAUser(userId).getBody();
        return user;

    }
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackEvent")
    @Bulkhead(name = "bulkheadTicketService", fallbackMethod = "fallbackTicket" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService", fallbackMethod = "fallbackTicket")
    public Event getEvent(String id){
        Event event = eventClient.getEvent(id).getBody();
        return event;
    }
    @CircuitBreaker(name = "ticketService", fallbackMethod = "fallbackGetTicket")
    @Bulkhead(name = "bulkheadTicketService", fallbackMethod = "fallbackGetTicket" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService", fallbackMethod = "fallbackGetTicket")
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
    @CircuitBreaker(name = "ticketService", fallbackMethod = "fallbackTicket")
    @Bulkhead(name = "bulkheadTicketService", fallbackMethod = "fallbackTicket" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService", fallbackMethod = "fallbackTicket")
    public Ticket createATicket(TicketRequest ticketRequest){
        try {
            BigDecimal price=BigDecimal.ZERO;
            Event event = getEvent(ticketRequest.getEventId());
            for (TicketItems ticketItems : event.getTicketItemsList()) {
                if (ticketItems.getLabel().equals(ticketRequest.getTicketItem())) {
                    if (ticketItems.getAvailableQuantity() <= 0) {
                        log.error("ticketsItem not available");
                        throw new Exception("no more avalible tickets");
                    } else {
                        ticketItems.setAvailableQuantity(ticketItems.getAvailableQuantity() - 1);
                        eventClient.updateEvent(EventDTOAdapter.mapEventToEventRequest(event),event.getId());


                        price= ticketItems.getPrice();
                    }
                }
            }
            Ticket ticket = TicketDTOAdapter.getTicket(ticketRequest);
            User user = userClient.getAUser(ticketRequest.getUserId()).getBody();
            String ticketNumber = UUID.randomUUID().toString() + "TN";
            ticket.setUser(user);
            ticket.setStatus(ACTIVE);
            ticket.setPrice(price);
            ticket.setTicketNumber(ticketNumber);
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
    @CircuitBreaker(name = "ticketService")
    @Bulkhead(name = "bulkheadTicketService" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService")
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
    @CircuitBreaker(name = "ticketService")
    @Bulkhead(name = "bulkheadTicketService" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService")
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
    @CircuitBreaker(name = "ticketService", fallbackMethod = "fallbackScanTicket")
    @Bulkhead(name = "bulkheadTicketService", fallbackMethod = "fallbackScanTicket" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryTicketService", fallbackMethod = "fallbackScanTicket")
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
    public Ticket fallbackTicket(TicketRequest ticketRequest, Throwable t) {
        Ticket ticket = new Ticket();
        ticket.setId("N/A");
        ticket.setTicketNumber("SERVICE NOT AVAILABLE");
        ticket.setTicketItem("SERVICE NOT AVAILABLE");

        return ticket;
    }
    public Ticket fallbackScanTicket(String id, Throwable t) {
        Ticket ticket = new Ticket();
        ticket.setId("N/A");
        ticket.setTicketNumber("SERVICE NOT AVAILABLE");
        ticket.setTicketItem("SERVICE NOT AVAILABLE");

        return ticket;
    }
    public Ticket fallbackGetTicket(String id, Throwable t) {
        Ticket ticket = new Ticket();
        ticket.setId("N/A");
        ticket.setTicketNumber("SERVICE NOT AVAILABLE");
        ticket.setTicketItem("SERVICE NOT AVAILABLE");

        return ticket;
    }

    public User fallbackUser(long id, Throwable throwable) {
        User fallbackUser = new User();
        fallbackUser.setId(id);
        fallbackUser.setUsername("N/A");
        fallbackUser.setEmail("N/A");
        fallbackUser.setDeleted(true);
        // Set other minimal data as needed

        return fallbackUser;
    }
    public Event fallbackEvent(String id, Throwable throwable) {
        Event fallbackEvent = new Event();
        fallbackEvent.setId(id);
        fallbackEvent.setDescription("event not available try again");
        return fallbackEvent;
    }


}

