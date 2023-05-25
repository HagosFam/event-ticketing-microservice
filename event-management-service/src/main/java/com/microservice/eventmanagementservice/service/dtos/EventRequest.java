package com.microservice.eventmanagementservice.service.dtos;

import com.microservice.clients.event.dtos.valueObjects.Address;
import com.microservice.clients.event.dtos.valueObjects.AgeRestriction;
import com.microservice.clients.event.dtos.valueObjects.EventType;
import com.microservice.clients.event.dtos.valueObjects.TicketItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
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
