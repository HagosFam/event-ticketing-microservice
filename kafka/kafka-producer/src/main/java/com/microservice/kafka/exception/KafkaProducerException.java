package com.microservice.kafka.exception;

import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducerException extends RuntimeException{
    public KafkaProducerException(String message){
        super(message);
    }
}
