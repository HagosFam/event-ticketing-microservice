package com.microservice.notificationservice.emailing.service.host;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@RequiredArgsConstructor
@Slf4j
public class NotifyHost {

    public void ticketBought(String eventId, String hostEmail){
        log.info("A ticket was bought for your event id:{}", eventId);

    }
    public void ticketRefunded(String eventId, String ticketId){
        log.info("A ticket refund request was submitted to event id:{} ticket id:{}", eventId, ticketId);

    }

    public void eventPosted(String eventId){
        log.info("A new event was created with id:{}", eventId);
    }
    public void eventUpdated(String eventId){
        log.info("An event was updated with id:{}", eventId);

    }
    public void eventDeleted(String eventId){
        log.info("an event was deleted with id :{}",eventId);
    }
    public void ticketScanned(String eventId, String ticketId){
        log.info("a ticket with id:{} was scanned for evend id:{}", ticketId,eventId);
    }

}
