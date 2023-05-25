package com.microservice.eventmanagementservice.models;


import com.microservice.clients.event.dtos.valueObjects.Address;
import com.microservice.clients.event.dtos.valueObjects.AgeRestriction;
import com.microservice.clients.event.dtos.valueObjects.EventType;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import com.microservice.clients.user.dtos.User;
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
    private User host;
    private String name;
    private String description;
    private LocalDate startdate;
    private LocalDate endDate;
    private Address location;
    private EventType eventType;
    private AgeRestriction ageRestriction;

    private List<TicketItems> ticketItemsList= new ArrayList<>();


}
