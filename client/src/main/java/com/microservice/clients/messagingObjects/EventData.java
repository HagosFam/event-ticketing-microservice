package com.microservice.clients.messagingObjects;

import com.microservice.clients.event.dtos.valueObjects.Address;
import com.microservice.clients.event.dtos.valueObjects.AgeRestriction;
import com.microservice.clients.event.dtos.valueObjects.EventType;
import com.microservice.clients.user.dtos.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventData {
  private String id;
  private long host;
  private String name;
  private String description;


  private Address location;
  private EventType eventType;
  private AgeRestriction ageRestriction;

}