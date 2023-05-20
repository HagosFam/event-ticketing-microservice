package com.microservice.clients.event.dtos.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketItems {

    private String label;
    private List<TicketItemEntry> ticketItemEntryList= new ArrayList<>();
    private long AvailableQuantity;
    private BigDecimal price;

}
