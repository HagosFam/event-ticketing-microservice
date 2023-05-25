package com.microservice.eventticketingservice.service.dtos;


import com.microservice.eventticketingservice.models.Ticket;

public class TicketDTOAdapter {
    public static Ticket getTicket(TicketRequest ticketRequest){
        Ticket ticket= new Ticket();

        ticket.setTicketItem(ticketRequest.getTicketItem());
        ticket.setDate(ticketRequest.getDate());

        return ticket;
    }
}
