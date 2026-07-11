package com.example.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
    String street,
    String city,
    String state,
    String zipCode,
    String country
) {}