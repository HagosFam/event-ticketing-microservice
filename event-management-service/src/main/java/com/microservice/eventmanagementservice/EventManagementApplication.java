package com.microservice.eventmanagementservice;

import com.microservice.eventmanagementservice.controller.EventController;
import com.microservice.eventmanagementservice.messaging.publisher.EventPublisher;
import com.microservice.eventmanagementservice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.w3c.dom.stylesheets.LinkStyle;
import schema_models.EventData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = {
        "com.microservice.kafka.service",
        "com.microservice.eventmanagementservice"
})
@RefreshScope
@EnableFeignClients(basePackages = "com.microservice.clients")

public class EventManagementApplication implements CommandLineRunner {
    @Autowired
    EventPublisher eventPublisher;

    public static void main(String[] args) {
        SpringApplication.run(EventManagementApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        Event event = new Event();
        event.setId("123456");
        event.setHostId(789);
        event.setName("Music Festival");
        event.setDescription("An exciting music festival with live performances");
        event.setStartdate(LocalDate.of(2023, 6, 1));
        event.setEndDate(LocalDate.of(2023, 6, 3));

        Address location = new Address();
        location.setStreet("123 Main Street");
        location.setCity("Cityville");
        location.setState("State");

        event.setLocation(location);

        event.setEventType(EventType.CONCERT);
        event.setAgeRestriction(AgeRestriction.MATURE);

        List<TicketItems> ticketItemsList = new ArrayList<>();
        TicketItems ticketItem1 = new TicketItems();
        ticketItem1.setLabel("General Admission");
        ticketItem1.setAvailableQuantity(100);
        ticketItem1.setPrice(BigDecimal.valueOf(50));

        TicketItems ticketItem2 = new TicketItems();
        ticketItem2.setLabel("VIP");
        ticketItem2.setAvailableQuantity(50);
        ticketItem2.setPrice(BigDecimal.valueOf(100.0));

        ticketItemsList.add(ticketItem1);
        ticketItemsList.add(ticketItem2);

        event.setTicketItemsList(ticketItemsList);
        eventPublisher.publish(event);

    }
}
