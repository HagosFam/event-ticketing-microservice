package com.microservice.clients.messagingObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderData {
   private String orderId;
   private List<String> ticketId;
   private Operation operation;

}
