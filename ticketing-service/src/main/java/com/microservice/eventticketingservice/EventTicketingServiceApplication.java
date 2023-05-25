package com.microservice.eventticketingservice;

import com.microservice.eventticketingservice.controller.TicketController;
import com.microservice.eventticketingservice.repository.TicketRepository;
import com.microservice.eventticketingservice.service.TicketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.microservice.clients")
@EnableEurekaClient
@PropertySources(
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
)
public class EventTicketingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketingServiceApplication.class, args);
    }

}
