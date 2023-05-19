package com.microservice.eventmanagementservice.models;

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
