package com.microservice.eventmanagementservice;

import com.microservice.eventmanagementservice.messaging.publisher.EventPublisher;
import com.microservice.eventmanagementservice.models.Event;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import schema_models.EventData;

import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = {
        "com.microservice.kafka.service"
})
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

    CommandLineRunner commandLineRunner(EventPublisher eventPublisher){
        EventData eventData = new EventData();
        eventData.setMessage("this is a message to you anaylitics");
        Event event = new Event();
        event.setId("sdasdsd");
        event.setHostId(789);
        event.setName("Music Festival");
        event.setDescription("An exciting music festival with live performances");
        event.setStartdate(LocalDate.of(2023, 6, 1));
        event.setEndDate(LocalDate.of(2023, 6, 3));
        return args -> {
            eventPublisher.publish(event);

        };
    }

}
