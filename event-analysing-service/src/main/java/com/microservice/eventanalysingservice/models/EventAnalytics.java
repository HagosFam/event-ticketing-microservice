package com.microservice.eventanalysingservice.models;

import com.microservice.clients.event.dtos.valueObjects.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAnalytics {
    @Id
    private String id;
    private Event event;
    private long totalTicketsSold;
    private long totalNumberOfTicket;
    private int totalTicketsRefunded;
    private BigDecimal totalRevenue;




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


}





