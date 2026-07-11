package com.example.service;

import com.example.dto.UserResponseDTO;
import com.example.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userDetails.getFirstName());
                    user.setLastName(userDetails.getLastName());
                    user.setJobTitle(userDetails.getJobTitle());
                    user.setDepartment(userDetails.getDepartment());

                    if (userDetails.getAddress() != null) {
                        user.setAddress(userDetails.getAddress());
                    }
                    if (userDetails.getPersonalContact() != null) {
                        user.setPersonalContact(userDetails.getPersonalContact());
                    }
                    if (userDetails.getWorkContact() != null) {
                        user.setWorkContact(userDetails.getWorkContact());
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByCity(String city) {
        return userRepository.findUsersByCity(city);
    }

    public Optional<User> getUserByPersonalEmail(String email) {
        return userRepository.findByPersonalEmail(email);
    }

    public Optional<User> getUserByWorkEmail(String email) {
        return userRepository.findByWorkEmail(email);
    }

    public UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getUsername(),
                user.getJobTitle(),
                user.getAddress(),
                user.getPersonalContact(),
                user.getWorkContact()
        );
    }
}