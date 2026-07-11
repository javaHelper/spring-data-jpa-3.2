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

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String description;

    // Instant type - stores precise moment in time (UTC)
    @Column(name = "event_time", columnDefinition = "TIMESTAMP")
    private Instant eventTime;

    // When event was created
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    // When event was last modified
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    // When event was cancelled (nullable)
    @Column(name = "cancelled_at", columnDefinition = "TIMESTAMP")
    private Instant cancelledAt;

    private String location;
    private String status; // SCHEDULED, ONGOING, COMPLETED, CANCELLED
}