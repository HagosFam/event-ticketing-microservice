package com.microservice.eventmanagementservice;

import com.microservice.eventmanagementservice.controller.EventController;
import com.microservice.eventmanagementservice.messaging.publisher.EventPublisher;
import com.microservice.eventmanagementservice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = {

        "com.microservice.eventmanagementservice"
})
@RefreshScope
@EnableFeignClients(basePackages = "com.microservice.clients")
@EnableEurekaClient
@PropertySources(
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
)
public class EventManagementApplication  {
    @Autowired
    EventPublisher eventPublisher;

    public static void main(String[] args) {
        SpringApplication.run(EventManagementApplication.class, args);
    }



}
