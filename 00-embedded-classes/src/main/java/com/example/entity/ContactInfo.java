package com.example.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record ContactInfo(
    String email,
    String phone,
    String website
) {}