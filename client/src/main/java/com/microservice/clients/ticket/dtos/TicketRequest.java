package com.microservice.clients.ticket.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TicketRequest {
    private long userId;
    private String ticketNumber;
    private String eventId;
    private String ticketItem;
    private LocalDate date;
    private BigDecimal price;
}