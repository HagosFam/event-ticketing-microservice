package com.example.orderservice.service;

import com.example.orderservice.models.Order;
import com.example.orderservice.models.enums.Status;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.dto.OrderDTOAdapter;
import com.example.orderservice.service.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createAnOrder(OrderRequest orderRequest){
        Order order = OrderDTOAdapter.getOrder(orderRequest);
        order.setStatus(Status.PENDING);
        orderRepository.save(order);
        return order;
    }
    public Order placeAnOrder(String orderId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
           if(makePayment(order.getTotalPrice())){
               order.setStatus(Status.COMPLETED);
               orderRepository.save(order);
               return order;
           }
           else{
               log.error("payment failed");
               return null;
           }

        }
        else {
            log.error("order not found");
            return null;
        }


    }
    public Order cancellAnOrder(String orderId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            makeRefund(orderId);
            order.setStatus(Status.CANCELLED);
            return order;
        }
        else {
            log.error("order not found");
            return null;
        }

    }

    // to be done asynchrounously
    private void makeRefund(String orderId) {
    }


    // to be done asynchrounously
    public boolean makePayment(BigDecimal amount){
        return true;
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();

    }
    public List<Order> getAllOrdersForEvent(String eventId){
        return orderRepository.findByOrderItemsEventId(eventId);

    }
    public List<Order> getAllOrdersForUser(String userId){
        return orderRepository.findByUserId(userId);
    }





}
