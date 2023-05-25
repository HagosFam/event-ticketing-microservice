package com.example.orderservice.messaging.publisher;

import com.example.orderservice.messaging.OrderMessaginMapper;
import com.example.orderservice.models.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.clients.messagingObjects.Operation;
import com.microservice.clients.messagingObjects.OrderData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {
    private final OrderMessaginMapper orderMessaginMapper;

    private final KafkaTemplate<String,String> kafkaTemplate;
    public void publish(Order order, Operation operation){
        try  {
        log.info("Received Order-placed event for order id:{}", order.getId());
        OrderData eventData = orderMessaginMapper.getOrderData(order);
        eventData.setOperation(operation);
        ObjectMapper objectMapper= new ObjectMapper();
        String payload = objectMapper.writeValueAsString(eventData);
        kafkaTemplate.send("order-placed",payload);
        log.info("message send successfully");

        }catch (Exception e){
          log.error("Error while sending eventData to kafka with event id:{} message: {}", order.getId(),e.getMessage());
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
