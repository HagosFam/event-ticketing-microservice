package com.example.usermanagementservice.controller;


import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.service.UserService;
import com.example.usermanagementservice.service.dtos.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

        private final UserService userService;



        @GetMapping
        public ResponseEntity<List<User>> getAllUsers() {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }

        @PostMapping
        public ResponseEntity<User> addNewUser(@RequestBody UserRequest userRequest) {
            User user = userService.addNewUser(userRequest);
            return ResponseEntity.ok(user);
        }

        @GetMapping("/{userId}")
        public ResponseEntity<User> getAUser(@PathVariable long userId) {
            User user = userService.getAUser(userId);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<User> deleteUser(@PathVariable long userId) {
            User user = userService.deleteUser(userId);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }



