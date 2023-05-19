package com.microservice.kafka.service.impl;

import com.microservice.kafka.exception.KafkaProducerException;
import com.microservice.kafka.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.io.Serializable;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerImpl <K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K,V> {

    private final KafkaTemplate<K,V> kafkaTemplate;
    @Override
    public void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
            log.info("Sending message={} to topic={}", message, topicName);
       try {
            ListenableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.addCallback(callback);
        }
       catch (KafkaException e){
           log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message, e.getMessage());
           throw  new KafkaProducerException("Error on kafka producer with key:"+key+" message:"+message+"and exception:"+e.getMessage());
           }
    }


    @PreDestroy
    public void close(){
        if(kafkaTemplate != null){
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }

    }

}
