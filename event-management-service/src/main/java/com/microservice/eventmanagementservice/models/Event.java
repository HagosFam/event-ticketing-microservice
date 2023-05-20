package com.microservice.eventmanagementservice.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    private String id;
    private long hostId;
    private String name;
    private String description;
    private LocalDate startdate;
    private LocalDate endDate;
    private Address location;
    private EventType eventType;
    private AgeRestriction ageRestriction;

    private List<TicketItems> ticketItemsList= new ArrayList<>();


}
