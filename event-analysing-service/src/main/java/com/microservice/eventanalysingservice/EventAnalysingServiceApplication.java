package com.microservice.eventanalysingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {
        "com.microservice.kafka.consumer"
})
@EnableEurekaClient

public class EventAnalysingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventAnalysingServiceApplication.class, args);
    }

}
