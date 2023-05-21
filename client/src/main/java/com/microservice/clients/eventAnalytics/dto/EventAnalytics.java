package com.microservice.clients.eventAnalytics.dto;

import com.microservice.clients.event.dtos.valueObjects.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAnalytics {
    private String id;
    private Event event;
    private List<String> processedTickets= new ArrayList<>();
    private long totalNumberOfTickets;
    private BigDecimal totalRevenue;

    public void addProcessedTickets(String ticketId) {
        this.processedTickets.add(ticketId);
    }
    public void addRevenue(BigDecimal amount) {
        this.totalRevenue = this.totalRevenue.add(amount);
    }


}





