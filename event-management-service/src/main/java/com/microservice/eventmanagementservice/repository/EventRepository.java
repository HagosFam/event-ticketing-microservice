package com.microservice.eventmanagementservice.repository;

import com.microservice.eventmanagementservice.models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event,String> {
    List<Event> findByHostId(long hostid);


}
