package com.microservice.eventmanagementservice;

import com.microservice.eventmanagementservice.controller.EventController;
import com.microservice.eventmanagementservice.messaging.publisher.EventPublisher;
import com.microservice.eventmanagementservice.models.Event;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import schema_models.EventData;

import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = {
        "com.microservice.kafka.service",
        "com.microservice.eventmanagementservice"
})
@EnableFeignClients(basePackages = "com.microservice.clients")

public class EventManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventManagementApplication.class, args);
    }



}
