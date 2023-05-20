package com.microservice.notificationservice.emailing.service.attnedee;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NotifyAtendee {
    public void ticketBought(String eventId, String ticketId){
        log.info("A ticket was bought you with id:{} for event id:{}", ticketId ,eventId);

    }
    public void ticketRefunded(String eventId, String ticketId){
        log.info("A ticket refund request was submitted by you for event id:{} and ticket id:{}", eventId, ticketId);

    }


    public void eventUpdated(String eventId){
        log.info("An event was updated with id:{}", eventId);

    }

    public void ticketScanned(String eventId, String ticketId){
        log.info("a ticket with id:{} was scanned for evend id:{}", ticketId,eventId);
    }



}
