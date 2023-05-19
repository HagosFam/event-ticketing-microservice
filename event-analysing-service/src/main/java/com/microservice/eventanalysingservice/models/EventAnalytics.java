package com.microservice.eventanalysingservice.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data

public class EventAnalytics {
    private String id;
    private String eventId;
    private long totalTicketsSold;

    private long totalNumberOfTicket;
    private int totalTicketsRefunded;
    private BigDecimal totalRevenue;
    private List<AttendeeAnalytics> attendeeAnalytics= new ArrayList<>();



    // Additional methods for updating analytics data

    public void incrementTicketsSold() {
        this.totalTicketsSold++;
    }

    public void incrementTicketsRefunded() {
        this.totalTicketsRefunded++;
    }

    public void addRevenue(BigDecimal amount) {
        this.totalRevenue = this.totalRevenue.add(amount);
    }

    public void addAttendeeAnalytics(AttendeeAnalytics attendeeAnalytics) {
        this.attendeeAnalytics.add(attendeeAnalytics);
    }
}





