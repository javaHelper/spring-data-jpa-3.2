package com.example.dto;

public record AddressDTO(
    String street,
    String city,
    String state,
    String zipCode,
    String country
) {}