package com.example.usermanagementservice;

import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.service.UserService;
import com.example.usermanagementservice.service.dtos.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testGetAllUsers() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        User user = podamFactory.manufacturePojo(User.class);
        User user1 = podamFactory.manufacturePojo(User.class);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(user1.getFirstName(), result.get(0).getFirstName());
        assertEquals(user.getFirstName(), result.get(1).getFirstName());

        verify(userRepository).findAll();
    }

    @Test
    void testAddNewUser() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        UserRequest userRequest = podamFactory.manufacturePojo(UserRequest.class);


        User result = userService.addNewUser(userRequest);

        assertNotNull(result);
        assertEquals(userRequest.getFirstName(), result.getFirstName());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetAUser_ExistingUser() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        User user = podamFactory.manufacturePojo(User.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getAUser(1L);

        assertNotNull(result);
        assertEquals(user.getFirstName(), result.getFirstName());

        verify(userRepository).findById(1L);
    }

    @Test
    void testGetAUser_NonExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getAUser(1L);

        assertNull(result);

        verify(userRepository).findById(1L);
    }

    @Test
    void testDeleteUser_ExistingUser() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        User user = podamFactory.manufacturePojo(User.class);

        when(userService.getAUser(1L)).thenReturn(user);

        User result = userService.deleteUser(1L);

        assertNotNull(result);
        assertTrue(result.isDeleted());

        verify(userRepository).save(user);
    }

}
