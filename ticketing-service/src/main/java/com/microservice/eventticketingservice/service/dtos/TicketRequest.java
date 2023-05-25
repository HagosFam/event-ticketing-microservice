package com.microservice.eventticketingservice.service.dtos;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TicketRequest {
    private long userId;

    private String eventId;
    private String ticketItem;
    private LocalDate date;

}