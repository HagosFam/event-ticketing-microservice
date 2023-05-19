package com.example.usermanagementservice.repository;

import com.example.usermanagementservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
