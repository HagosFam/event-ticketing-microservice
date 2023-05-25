package com.example.orderservice;

import com.example.orderservice.messaging.publisher.EventPublisher;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.enums.Status;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.dto.OrderRequest;
import com.microservice.clients.eventAnalytics.EventAnalysisClient;
import com.microservice.clients.messagingObjects.Operation;
import com.microservice.clients.ticket.TicketClient;
import com.microservice.clients.ticket.dtos.Ticket;
import com.microservice.clients.user.UserClient;
import com.microservice.clients.user.dtos.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceApplicationTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EventAnalysisClient eventAnalysisClient;

    @Mock
    private TicketClient ticketClient;

    @Mock
    private UserClient userClient;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    PodamFactory podamFactory= new PodamFactoryImpl();

    @Test
    void getTicket_WithValidTicketId_ReturnsTicket() {
        // Arrange
        String ticketId = "12345";
        Ticket expectedTicket = podamFactory.manufacturePojo(Ticket.class);
        when(ticketClient.getTicketById(ticketId)).thenReturn(ResponseEntity.ok(expectedTicket));

        // Act
        Ticket result = orderService.getTicket(ticketId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTicket, result);
        verify(ticketClient, times(1)).getTicketById(ticketId);
    }



    @Test
    void refundTicket_WithValidTicketId_RefundsTicket() {
        // Arrange
        String ticketId = "12345";

        // Act
        orderService.refundTicket(ticketId);

        // Assert
        verify(ticketClient, times(1)).refundTicket(ticketId);
    }

    @Test
    void getUser_WithValidUserId_ReturnsUser() {
        // Arrange
        long userId = 1L;
        User expectedUser = podamFactory.manufacturePojo(User.class);
        when(userClient.getAUser(userId)).thenReturn(ResponseEntity.ok(expectedUser));

        // Act
        User result = orderService.getUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userClient, times(1)).getAUser(userId);
    }


    @Test
    void placeAnOrder_WithValidOrderRequest_PlacesOrderAndReturnsCompletedOrder() throws InterruptedException {
        // Arrange
        OrderRequest orderRequest = podamFactory.manufacturePojo(OrderRequest.class);
        Ticket ticket1 = podamFactory.manufacturePojo(Ticket.class);
        Ticket ticket2 = podamFactory.manufacturePojo(Ticket.class);
        ticket1.setPrice(BigDecimal.valueOf(10));
        ticket2.setPrice(BigDecimal.valueOf(20));
        List<String> ticketIds = Arrays.asList(ticket1.getId(), ticket2.getId());
        orderRequest.setTicketId(ticketIds);
        User user = podamFactory.manufacturePojo(User.class);
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(30);
        when(ticketClient.getTicketById(ticket1.getId())).thenReturn(ResponseEntity.ok(ticket1));
        when(ticketClient.getTicketById(ticket2.getId())).thenReturn(ResponseEntity.ok(ticket2));
        when(userClient.getAUser(orderRequest.getUserId())).thenReturn(ResponseEntity.ok(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.placeAnOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(Status.COMPLETED, result.getStatus());
        assertEquals(expectedTotalPrice, result.getTotalPrice());
        assertEquals(user, result.getUser());
        assertEquals(2, result.getTickets().size());
        assertTrue(result.getTickets().containsAll(Arrays.asList(ticket1, ticket2)));
        verify(ticketClient, times(1)).getTicketById(ticket1.getId());
        verify(ticketClient, times(1)).getTicketById(ticket2.getId());
        verify(userClient, times(1)).getAUser(orderRequest.getUserId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(eventPublisher, times(1)).publish(result, Operation.BOUGHT);
    }


    @Test
    void cancellAnOrder_WithInvalidOrderId_ReturnsNull() {
        // Arrange
        String orderId = "12345";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Order result = orderService.cancellAnOrder(orderId);

        // Assert
        assertNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
        verify(eventPublisher, never()).publish(any(Order.class), any(Operation.class));
    }

    @Test
    void getAllOrders_ReturnsAllOrders() {
        // Arrange
        List<Order> expectedOrders = Arrays.asList(podamFactory.manufacturePojo(Order.class), podamFactory.manufacturePojo(Order.class));
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(expectedOrders, result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrdersForEvent_WithValidEventId_ReturnsOrdersForEvent() {
        // Arrange
        String eventId = "12345";
        List<Order> expectedOrders = Arrays.asList(podamFactory.manufacturePojo(Order.class), podamFactory.manufacturePojo(Order.class));
        when(orderRepository.findByTicketsEventId(eventId)).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.getAllOrdersForEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOrders, result);
        verify(orderRepository, times(1)).findByTicketsEventId(eventId);
    }

    @Test
    void getAllOrdersForUser_WithValidUserId_ReturnsOrdersForUser() {
        // Arrange
        long userId = 1L;
        List<Order> expectedOrders = Arrays.asList(podamFactory.manufacturePojo(Order.class), podamFactory.manufacturePojo(Order.class));
        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.getAllOrdersForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOrders, result);
        verify(orderRepository, times(1)).findByUserId(userId);
    }
}

