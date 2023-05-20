package com.microservice.clients.event.dtos.valueObjects;


import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
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
