package com.microservice.eventanalysingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.microservice.kafka.consumer"
})
public class EventAnalysingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventAnalysingServiceApplication.class, args);
    }

}
