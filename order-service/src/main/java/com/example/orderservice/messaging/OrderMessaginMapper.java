package com.example.orderservice.messaging;

import com.example.orderservice.models.Order;
import com.microservice.clients.messagingObjects.OrderData;
import org.springframework.stereotype.Component;
@Component
public class OrderMessaginMapper {

    public OrderData getOrderData(Order order){
        return new OrderData().builder()
                .orderId(order.getId())
                .ticketId(order.getTickets().stream().map(x->x.getId()).toList()).build();
    }

}
