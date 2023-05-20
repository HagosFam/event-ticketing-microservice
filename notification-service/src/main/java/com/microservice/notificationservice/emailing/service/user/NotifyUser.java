package com.microservice.notificationservice.emailing.service.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@RequiredArgsConstructor
@Slf4j
public class NotifyUser {
    public void userRegistered(String userId){
        log.info("you just register to ticketMaster");

    }
}
