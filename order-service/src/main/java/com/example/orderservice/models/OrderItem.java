package com.example.orderservice.models;

import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.ticket.dtos.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Event eventId;
    private Ticket ticketId;
    private BigDecimal price;
}
