package com.microservice.clients.user;

import com.microservice.clients.user.dtos.User;
import com.microservice.clients.user.dtos.UserRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class UserClientFallback implements UserClient{
    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        return null;
    }

    @Override
    public ResponseEntity<User> addNewUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public ResponseEntity<User> getAUser(long userId) {
        return null;
    }

    @Override
    public ResponseEntity<User> deleteUser(long userId) {
        return null;
    }
}
