package com.microservice.eventanalysingservice.models;

import java.math.BigDecimal;

public class AttendeeAnalytics {
    private String attendeeId;
    private int ticketsPurchased;
    private BigDecimal totalSpent;

    // Constructors, getters, and setters

    public AttendeeAnalytics(String attendeeId) {
        this.attendeeId = attendeeId;
        this.ticketsPurchased = 0;
        this.totalSpent = BigDecimal.ZERO;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public int getTicketsPurchased() {
        return ticketsPurchased;
    }

    public void setTicketsPurchased(int ticketsPurchased) {
        this.ticketsPurchased = ticketsPurchased;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    // Additional methods for updating attendee analytics data

    public void incrementTicketsPurchased() {
        this.ticketsPurchased++;
    }

    public void addSpentAmount(BigDecimal amount) {
        this.totalSpent = this.totalSpent.add(amount);
    }
}
