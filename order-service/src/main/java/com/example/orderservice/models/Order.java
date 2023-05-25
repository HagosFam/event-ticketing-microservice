package com.example.orderservice.models;

import com.example.orderservice.models.enums.Status;
import com.google.common.base.Ticker;
import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.clients.user.dtos.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;
    private User user;
    private List<Ticket> tickets= new ArrayList<>();
    private BigDecimal totalPrice;
    private Status status;

    public void addtickets(Ticket orderItem){
        this.tickets.add(orderItem);
    }

}
