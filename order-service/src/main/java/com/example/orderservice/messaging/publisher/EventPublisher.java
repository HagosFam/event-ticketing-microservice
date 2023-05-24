package com.microservice.eventmanagementservice.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.clients.messagingObjects.EventData;
import com.microservice.eventmanagementservice.messaging.EventMessagingMapper;
import com.microservice.eventmanagementservice.models.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {
    private final EventMessagingMapper eventMessagingMapper;

    private final KafkaTemplate<String,String> kafkaTemplate;
    public void publish(Event event){
        try  {
        log.info("Received Event-created event for event id:{}", event.getId());
        EventData eventData = eventMessagingMapper.getEventData(event);
            ObjectMapper objectMapper= new ObjectMapper();
            String s = objectMapper.writeValueAsString(eventData);

            kafkaTemplate.send("event-created",s);
        log.info("message send successfully");

        }catch (Exception e){
          log.error("Error while sending eventData to kafka with event id:{} message: {}", event.getId(),e.getMessage());
      }


    }
//    private final EventMessagingConfigurationData eventMessagingConfigurationData;
//    private final KafkaProducer<String, EventData> kafkaProducer;
//    public void publish(Event event){
//      try  {
//            String key = event.getId();
//            log.info("Received Event-created event for event id:{}", key);
//            EventData eventData = eventMessagingMapper.getEventData(event);
//
//            kafkaProducer.send("event-created", key, eventData, kafkaCallback("event-created", eventData));
//            log.info("eventData sent to kafka for event id:{}", event.getId());
//        }catch (Exception e){
//          log.error("Error while sending eventData to kafka with event id:{} message: {}", event.getId(),e.getMessage());
//      }
//    }
//
//    private ListenableFutureCallback<SendResult<String, EventData>>
//    kafkaCallback(String eventCreatedResponseTopicName, EventData eventData) {
//        return new ListenableFutureCallback<SendResult<String, EventData>>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                log.error("Error while sending EventData message: {} topic:{}", eventData.toString(),eventCreatedResponseTopicName,ex);
//            }
//
//            @Override
//            public void onSuccess(SendResult<String, EventData> result) {
//                RecordMetadata metadata= result.getRecordMetadata();
//                log.info("Received successful response from kafka for event id: {} "+ "Topic: {} Partition: {} Offset: {} Timestamp: {}",
//                        "someId",
//                        metadata.topic(),
//                        metadata.partition(),
//                        metadata.offset(),
//                        metadata.timestamp());
//
//            }
//        };
//
//    }

}
