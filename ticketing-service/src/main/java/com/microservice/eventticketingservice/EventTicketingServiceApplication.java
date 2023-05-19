package com.microservice.eventticketingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.microservice.clients"
        }

)
public class EventTicketingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketingServiceApplication.class, args);
    }

}
