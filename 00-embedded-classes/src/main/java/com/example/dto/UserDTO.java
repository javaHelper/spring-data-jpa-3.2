package com.example.dto;

public record UserDTO(
    Long id,
    String firstName,
    String lastName,
    String username,
    String jobTitle,
    AddressDTO address,
    ContactInfoDTO workContact
) {}