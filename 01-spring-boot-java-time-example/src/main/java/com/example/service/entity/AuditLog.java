package com.example.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.Year;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityType;
    private Long entityId;
    private String action; // CREATE, UPDATE, DELETE
    private String changedBy;

    // Precise timestamp of change
    @Column(name = "change_timestamp", columnDefinition = "TIMESTAMP")
    private Instant changeTimestamp;

    // Year for reporting purposes
    @Column(name = "audit_year", columnDefinition = "INT")
    private Year auditYear;

    private String oldValue;
    private String newValue;
    private String description;
}