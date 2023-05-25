package com.example.orderservice.service.dto;

import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.ticket.dtos.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
        private String ticket;


}
