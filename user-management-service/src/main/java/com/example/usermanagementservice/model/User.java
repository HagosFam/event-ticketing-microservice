package com.example.usermanagementservice.model;

import com.example.usermanagementservice.model.enums.Gender;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue
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
