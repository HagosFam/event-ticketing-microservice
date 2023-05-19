package com.microservice.eventmanagementservice.messaging.publisher;

import com.microservice.eventmanagementservice.config.EventMessagingConfigurationData;
import com.microservice.eventmanagementservice.messaging.EventMessagingMapper;
import com.microservice.eventmanagementservice.models.Event;
import com.microservice.kafka.service.KafkaProducer;
import com.microservice.kafka.service.impl.KafkaProducerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import schema_models.EventData;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {
    private final EventMessagingMapper eventMessagingMapper;
    private final EventMessagingConfigurationData eventMessagingConfigurationData;
    private final KafkaProducer<String, EventData> kafkaProducer;
    public void publish(Event event){
      try  {
            String key = event.getId();
            log.info("Received Event-created event for event id:{}", key);
            EventData eventData = eventMessagingMapper.getEventData(event);
            kafkaProducer.send("event-created", key, eventData, kafkaCallback("event-is-created", eventData));
            log.info("eventData sent to kafka for event id:{}", event.getId());
        }catch (Exception e){
          log.error("Error while sending eventData to kafka with event id:{}", event.getId());
      }
    }

    private ListenableFutureCallback<SendResult<String, EventData>>
    kafkaCallback(String eventCreatedResponseTopicName, EventData eventData) {
        return new ListenableFutureCallback<SendResult<String, EventData>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending EventData message: {} topic:{}", eventData.toString(),eventCreatedResponseTopicName,ex);
            }

            @Override
            public void onSuccess(SendResult<String, EventData> result) {
                RecordMetadata metadata= result.getRecordMetadata();
                log.info("Received successful response from kafka for event id: {} "+ "Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        "someId",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());

            }
        };

    }

}
