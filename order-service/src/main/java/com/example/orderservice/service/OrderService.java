package com.example.orderservice.service;

import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderItem;
import com.example.orderservice.models.enums.Status;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.dto.OrderDTOAdapter;
import com.example.orderservice.service.dto.OrderRequest;
import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.clients.ticket.TicketClient;
import com.microservice.clients.ticket.dtos.enums.TicketStatus;
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
    private final EventAnalysisClient eventAnalysisClient;
    private final TicketClient ticketClient;


    public Order placeAnOrder(OrderRequest orderRequest){
        Order order = OrderDTOAdapter.getOrder(orderRequest);

            if(makePayment(order.getTotalPrice())){
               order.setStatus(Status.COMPLETED);
               orderRepository.save(order);
               for(OrderItem orderItem: order.getOrderItems()){
                   eventAnalysisClient.addTicketSales(orderItem.getTicket().getId());
               }
               return order;
           }
           else{
               log.error("payment failed");
               return null;
           }
    }
    public Order cancellAnOrder(String orderId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            makeRefund(orderId);
            order.setStatus(Status.CANCELLED);
            for(OrderItem orderItem: order.getOrderItems()){
                eventAnalysisClient.addTicketSales(orderItem.getTicket().getId());
            }
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
