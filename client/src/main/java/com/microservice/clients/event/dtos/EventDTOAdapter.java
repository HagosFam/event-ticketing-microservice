package com.microservice.clients.event.dtos;

import com.microservice.clients.event.dtos.valueObjects.Event;


public class EventDTOAdapter {
    public static Event getEvent(EventRequest eventRequest){
        Event event= new Event();
        event.setDescription(eventRequest.getDescription());
        event.setName(eventRequest.getName());
        event.setStartdate(eventRequest.getStartdate());
        event.setEndDate(eventRequest.getEndDate());
        event.setAgeRestriction(eventRequest.getAgeRestriction());
        event.setLocation(eventRequest.getLocation());
        event.setEventType(eventRequest.getEventType());
        event.setTicketItemsList(eventRequest.getTicketItemsList());
        return event;
    }
    public static EventRequest mapEventToEventRequest(Event event) {
        EventRequest eventRequest = new EventRequest();
        eventRequest.setHostId(event.getHostId());
        eventRequest.setName(event.getName());
        eventRequest.setDescription(event.getDescription());
        eventRequest.setStartdate(event.getStartdate());
        eventRequest.setEndDate(event.getEndDate());
        eventRequest.setLocation(event.getLocation());
        eventRequest.setEventType(event.getEventType());
        eventRequest.setAgeRestriction(event.getAgeRestriction());
        eventRequest.setTicketItemsList(event.getTicketItemsList());
        return eventRequest;
    }


}
