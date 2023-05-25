package com.example.orderservice.service;

import com.example.orderservice.messaging.publisher.EventPublisher;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.enums.Status;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.dto.OrderRequest;
import com.microservice.clients.event.EventClient;
import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.clients.messagingObjects.Operation;
import com.microservice.clients.ticket.TicketClient;
import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.clients.user.UserClient;
import com.microservice.clients.user.dtos.User;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final EventAnalysisClient eventAnalysisClient;
    private final TicketClient ticketClient;
    private final UserClient userClient;
    private final EventPublisher eventPublisher;

    @CircuitBreaker(name = "ticketService", fallbackMethod = "fallbackGetTicket")
    @Bulkhead(name = "bulkheadOrderService", fallbackMethod = "fallbackGetTicket", type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryOrderService", fallbackMethod = "fallbackGetTicket")
    public Ticket getTicket(String ticketId){
        return ticketClient.getTicketById(ticketId).getBody();
    }
    public Ticket fallbackGetTicket() {
        Ticket ticket = new Ticket();
        ticket.setId("N/A");
        ticket.setTicketNumber("SERVICE NOT AVAILABLE");
        ticket.setTicketItem("SERVICE NOT AVAILABLE");

        return ticket;
    }
    @CircuitBreaker(name = "ticketService")
    @Bulkhead(name = "bulkheadOrderService", fallbackMethod = "fallbackGetTicket", type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryOrderService", fallbackMethod = "fallbackGetTicket")

    public void refundTicket(String ticketId){
        ticketClient.refundTicket(ticketId);
    }
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    @Bulkhead(name = "bulkheadOrderService", fallbackMethod = "fallbackUser", type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryOrderService", fallbackMethod = "fallbackUser")

    public User getUser(long userId){
        User user = userClient.getAUser(userId).getBody();
        return user;

    }
    public User fallbackUser(long id, Throwable throwable) {
        User fallbackUser = new User();
        fallbackUser.setId(id);
        fallbackUser.setUsername("N/A");
        fallbackUser.setEmail("N/A");
        fallbackUser.setDeleted(true);
        // Set other minimal data as needed

        return fallbackUser;
    }



    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackOrder")
    @Bulkhead(name = "bulkheadOrderService", fallbackMethod = "fallbackOrder" ,type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryOrderService", fallbackMethod = "fallbackOrder")
    public Order placeAnOrder(OrderRequest orderRequest) throws InterruptedException {
        Order order = new Order();
        order.setStatus(Status.PENDING);

        for(String ticketId: orderRequest.getTicketId()){
            Ticket ticket = getTicket(ticketId);
            System.out.println(ticket+">>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<");
            order.addtickets(ticket);
        }
        order.setTotalPrice(order.getTickets()
                .stream().map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        User user = getUser(orderRequest.getUserId());
        order.setUser(user);

        if(makePayment(order.getTotalPrice())){
               order.setStatus(Status.COMPLETED);
               orderRepository.save(order);
               eventPublisher.publish(order, Operation.BOUGHT);
               return order;
           }
           else{
               log.error("payment failed");
               return null;
           }
    }
    @CircuitBreaker(name = "orderService", fallbackMethod ="fallbackCancelOrder" )
    @Bulkhead(name = "bulkheadOrderService", fallbackMethod = "fallbackCancelOrder", type = Bulkhead.Type.THREADPOOL)
    @Retry(name="retryOrderService", fallbackMethod = "fallbackCancelOrder")
    public Order cancellAnOrder(String orderId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            makeRefund(orderId);
            order.setStatus(Status.CANCELLED);
            eventPublisher.publish(order, Operation.REFUNDED);
            return order;
        }
        else {
            log.error("order not found");
            return null;
        }

    }
    public Order fallbackOrder(OrderRequest orderRequest, Throwable throwable) {
        Order order = new Order();
        order.setId("UNAVAILABLE");
        return order;
    }
    public Order fallbackCancelOrder(String tickdeid, Throwable throwable) {
        Order order = new Order();
        order.setId("UNAVAILABLE");
        return order;
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
        return orderRepository.findByTicketsEventId(eventId);

    }
    public List<Order> getAllOrdersForUser(long userId){
        return orderRepository.findByUserId(userId);
    }





}
