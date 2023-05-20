package com.microservice.clients.user.dtos;

import com.microservice.clients.user.dtos.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastname;
    private LocalDate dateOfBirth;
    private Gender gender;
    private boolean deleted;

}
