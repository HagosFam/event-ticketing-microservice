package com.microservice.eventanalysingservice.repository;

import com.microservice.clients.event.dtos.valueObjects.Event;
import com.microservice.eventanalysingservice.models.EventAnalytics;
import com.microservice.eventanalysingservice.service.EventAnalyticsService;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventAnalysisRepository extends MongoRepository<EventAnalyticsService,String> {

    Optional<EventAnalytics> findByEvent(Event event);

}
