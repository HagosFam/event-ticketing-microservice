package com.example.orderservice.models;

import com.example.orderservice.models.enums.Status;
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
    private String userId;
    private List<OrderItem> orderItems= new ArrayList<>();
    private BigDecimal totalPrice;
    private Status status;

}
