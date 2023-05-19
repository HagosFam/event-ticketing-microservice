package com.example.orderservice.service.dto;

import com.example.orderservice.models.Order;

public class OrderDTOAdapter {

    public static Order getOrder(OrderRequest orderRequest){
        return  new Order().builder()
                .orderItems(orderRequest.getOrderItems())
                .userId(orderRequest.getUserId())
                .totalPrice(orderRequest.getTotalPrice())
                .build();

    }

}
