package com.microservice.eventmanagementservice.service.dtos;

import com.microservice.eventmanagementservice.models.Address;
import com.microservice.eventmanagementservice.models.AgeRestriction;
import com.microservice.eventmanagementservice.models.EventType;
import com.microservice.eventmanagementservice.models.TicketItems;
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
