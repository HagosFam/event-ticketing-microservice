package com.example.orderservice.service.dto;

import com.example.orderservice.models.OrderItem;
import com.example.orderservice.models.enums.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data

public class OrderRequest {
    private String userId;
    private List<OrderItem> orderItems= new ArrayList<>();
    private BigDecimal totalPrice;
    private Status staus;
}
