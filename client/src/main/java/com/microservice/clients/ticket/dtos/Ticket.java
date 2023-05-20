package com.microservice.clients.ticket.dtos;

import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.ticket.dtos.enums.TicketStatus;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {
    private String id;
    private long userId;
    private String ticketNumber;
    private Event event;
    private String ticketItem;
    private LocalDate date;
    private BigDecimal price;



    private TicketStatus status;
    private boolean isDeleted= false;
}
