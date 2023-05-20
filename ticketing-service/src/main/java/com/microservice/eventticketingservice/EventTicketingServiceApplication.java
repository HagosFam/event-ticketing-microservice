package com.microservice.eventticketingservice;

import com.microservice.eventticketingservice.controller.TicketController;
import com.microservice.eventticketingservice.repository.TicketRepository;
import com.microservice.eventticketingservice.service.TicketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.microservice.clients")
public class EventTicketingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketingServiceApplication.class, args);
    }

}
