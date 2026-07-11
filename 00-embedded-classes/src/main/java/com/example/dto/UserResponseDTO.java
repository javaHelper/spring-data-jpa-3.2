package com.example.dto;

import com.example.entity.Address;
import com.example.entity.ContactInfo;

public record UserResponseDTO(
    Long id,
    String fullName,
    String username,
    String jobTitle,
    Address address,
    ContactInfo personalContact,
    ContactInfo workContact
) {}