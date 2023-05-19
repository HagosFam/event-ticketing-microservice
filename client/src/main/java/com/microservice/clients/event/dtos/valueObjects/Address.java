package com.microservice.clients.event.dtos.valueObjects;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Address {
    private String street;
    private String state;
    private String city;
    private String country;

}
