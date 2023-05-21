package com.microservice.eventticketingservice.service.dtos;

import com.microservice.clients.user.dtos.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TicketRequest {
    private User user;
    private String ticketNumber;
    private String eventId;
    private String ticketItem;
    private LocalDate date;
    private BigDecimal price;
}