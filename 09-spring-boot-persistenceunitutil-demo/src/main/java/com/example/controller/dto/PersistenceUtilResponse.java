package com.example.controller.dto;

public record PersistenceUtilResponse(
        Long version,
        boolean entityLoaded,
        boolean profileLoaded,
        boolean isEmployee,
        String entityClass
) {
}