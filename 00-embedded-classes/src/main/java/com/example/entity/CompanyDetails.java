package com.example.entity;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record CompanyDetails(
    String registrationNumber,
    String taxId,
    LocalDate foundedDate,
    String industry
) {}