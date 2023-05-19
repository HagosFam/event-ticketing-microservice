package com.microservice.clients.event.dtos.valueObjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TicketItems {

    private String label;
    private List<TicketItemEntry> ticketItemEntryList= new ArrayList<>();
    private long AvailableQuantity;
    private BigDecimal price;

}
