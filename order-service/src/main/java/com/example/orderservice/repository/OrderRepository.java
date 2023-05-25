package com.example.orderservice.repository;

import com.example.orderservice.models.Order;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order,String > {
    List<Order> findByUserId(long userId);
    List<Order> findByTicketsEventId(String eventId);
}
