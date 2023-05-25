package com.microservice.eventticketingservice;

import com.microservice.clients.event.EventClient;
import com.microservice.clients.event.dtos.EventRequest;
import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.clients.user.UserClient;
import com.microservice.clients.user.dtos.User;
import com.microservice.eventticketingservice.models.Ticket;
import com.microservice.eventticketingservice.models.enums.TicketStatus;
import com.microservice.eventticketingservice.repository.TicketRepository;
import com.microservice.eventticketingservice.service.TicketService;
import com.microservice.eventticketingservice.service.dtos.TicketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.microservice.eventticketingservice.models.enums.TicketStatus.ACTIVE;
import static com.microservice.eventticketingservice.models.enums.TicketStatus.USED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EventTicketingServiceApplicationTests {
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventClient eventClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    PodamFactory podamFactory= new PodamFactoryImpl();

    @Test
    void getUser_WithValidUserId_ReturnsUser() {
        // Arrange
        long userId = 1L;
        User expectedUser = podamFactory.manufacturePojo(User.class);
        ResponseEntity<User> responseEntity = new ResponseEntity<>(expectedUser, HttpStatus.OK);
        when(userClient.getAUser(userId)).thenReturn(responseEntity);

        // Act
        User result = ticketService.getUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userClient, times(1)).getAUser(userId);
    }

    @Test
    void getEvent_WithValidEventId_ReturnsEvent() {
        // Arrange
        String eventId = "12345";
        Event expectedEvent = podamFactory.manufacturePojo(Event.class);
        ResponseEntity<Event> responseEntity = new ResponseEntity<>(expectedEvent, HttpStatus.OK);
        when(eventClient.getEvent(eventId)).thenReturn(responseEntity);

        // Act
        Event result = ticketService.getEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedEvent, result);
        verify(eventClient, times(1)).getEvent(eventId);
    }

    @Test
    void getTicketFromId_WithValidTicketId_ReturnsTicket() {
        // Arrange
        String ticketId = "12345";
        Ticket expectedTicket = podamFactory.manufacturePojo(Ticket.class);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(expectedTicket));

        // Act
        Ticket result = ticketService.getTicketFromId(ticketId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTicket, result);
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void getTicketFromId_WithInvalidTicketId_ReturnsNull() {
        // Arrange
        String ticketId = "12345";
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act
        Ticket result = ticketService.getTicketFromId(ticketId);

        // Assert
        assertNull(result);
        verify(ticketRepository, times(1)).findById(ticketId);
    }



    @Test
    void ticketRefund_WithInvalidTicketId_DoesNothing() {
        // Arrange
        String ticketId = "12345";
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act
        ticketService.ticketRefund(ticketId);

        // Assert
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(eventClient, never()).updateEvent(any(EventRequest.class), anyString());
    }



    @Test
    void deleteTicket_WithInvalidTicketId_DoesNothing() {
        // Arrange
        String ticketId = "12345";
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act
        ticketService.deleteTicket(ticketId);

        // Assert
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void ticketScan_WithValidTicketId_ReturnsScannedTicket() {
        // Arrange
        String ticketId = "12345";
        Ticket ticket = podamFactory.manufacturePojo(Ticket.class);
        ticket.setStatus(ACTIVE);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket result = ticketService.ticketScan(ticketId);

        // Assert
        assertNotNull(result);
        assertEquals(USED, result.getStatus());
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void ticketScan_WithInvalidTicketId_ReturnsNull() {
        // Arrange
        String ticketId = "12345";
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act
        Ticket result = ticketService.ticketScan(ticketId);

        // Assert
        assertNull(result);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
