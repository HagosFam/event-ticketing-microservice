package com.microservice.eventanalysingservice.models;

import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.clients.ticket.dtos.TicketRequest;
import com.microservice.clients.user.dtos.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AttendeeAnalytics {
    private User attendee;
    private List<Ticket> ticketsPurchased;
    private BigDecimal totalSpent;

    // Constructors, getters, and setters

    public AttendeeAnalytics(User attendee) {
        this.attendee = attendee;
        this.ticketsPurchased = new ArrayList<>();
        this.totalSpent = BigDecimal.ZERO;
    }

    public User getAttendeeId() {return attendee;}

    public void setAttendeeId(User attendee) {
        this.attendee = attendee;
    }

    public List<Ticket> getTicketsPurchased() {
        return ticketsPurchased;
    }

    public void setTicketsPurchased(List<Ticket> ticketsPurchased) {
        this.ticketsPurchased = ticketsPurchased;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    // Additional methods for updating attendee analytics data

    public List<Ticket> incrementTicketsPurchased(Ticket t) {
        this.ticketsPurchased.add(t);
        updateTotalSpentAmount(t.getPrice());
        return ticketsPurchased;
    }

    public BigDecimal updateTotalSpentAmount(BigDecimal price){
      this.totalSpent= this.totalSpent.add(price);
      return totalSpent;


    }
}
