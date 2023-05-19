package com.microservice.eventticketingservice.models;

import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.eventticketingservice.models.enums.TicketStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {
    @Id
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
