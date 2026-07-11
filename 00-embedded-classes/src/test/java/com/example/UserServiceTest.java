package com.example;

import com.example.entity.Address;
import com.example.entity.ContactInfo;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Address testAddress;
    private ContactInfo testContact;

    @BeforeEach
    void setUp() {
        testAddress = new Address("123 Main St", "New York", "NY", "10001", "USA");
        testContact = new ContactInfo("test@example.com", "555-0101", "www.test.com");

        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setUsername("johndoe");
        testUser.setAddress(testAddress);
        testUser.setPersonalContact(testContact);
        testUser.setJobTitle("Engineer");
        testUser.setDepartment("Engineering");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        assertNotNull(createdUser);
        assertEquals("johndoe", createdUser.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUsersByCity() {
        when(userRepository.findUsersByCity("New York")).thenReturn(java.util.List.of(testUser));

        var users = userService.getUsersByCity("New York");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findUsersByCity("New York");
    }
}
