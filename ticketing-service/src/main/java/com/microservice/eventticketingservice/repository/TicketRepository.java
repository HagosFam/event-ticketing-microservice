package com.microservice.eventticketingservice.repository;


import com.microservice.eventticketingservice.models.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket,String> {
}
