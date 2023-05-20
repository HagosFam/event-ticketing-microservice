package com.microservice.clients.user;

import com.microservice.clients.user.dtos.User;
import com.microservice.clients.user.dtos.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users")
    ResponseEntity<List<User>> getAllUsers();

    @PostMapping("/api/users")
    ResponseEntity<User> addNewUser(@RequestBody UserRequest userRequest);

    @GetMapping("/api/users/{userId}")
    ResponseEntity<User> getAUser(@PathVariable("userId") long userId);

    @DeleteMapping("/api/users/{userId}")
    ResponseEntity<User> deleteUser(@PathVariable("userId") long userId);
}
