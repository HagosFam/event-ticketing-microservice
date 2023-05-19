package com.microservice.eventmanagementservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix="event-service")
public class EventMessagingConfigurationData {
    private String eventCreatedRequestTopicName;
    private String eventCreatedResponseTopicName;
    private String eventUpdatedTopicName;
    private String eventDeletedTopicName;
    private String eventStartedTopicName;

}
