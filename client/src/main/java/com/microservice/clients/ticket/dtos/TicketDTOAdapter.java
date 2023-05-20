package com.microservice.clients.ticket.dtos;



public class TicketDTOAdapter {
    public static Ticket getTicket(TicketRequest ticketRequest){
        Ticket ticket= new Ticket();
        ticket.setTicketNumber(ticketRequest.getTicketNumber());
        ticket.setTicketItem(ticketRequest.getTicketItem());
        ticket.setDate(ticketRequest.getDate());
        ticket.setUserId(ticketRequest.getUserId());
        return ticket;
    }
}
