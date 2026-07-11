package com.example.service.repository;

import com.example.service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    // Find events after a specific Instant
    @Query("SELECT e FROM Event e WHERE e.eventTime > :afterTime ORDER BY e.eventTime ASC")
    List<Event> findEventsAfter(@Param("afterTime") Instant afterTime);

    // Find events before a specific Instant
    @Query("SELECT e FROM Event e WHERE e.eventTime < :beforeTime ORDER BY e.eventTime DESC")
    List<Event> findEventsBefore(@Param("beforeTime") Instant beforeTime);

    // Find events between two Instants
    @Query("SELECT e FROM Event e WHERE e.eventTime BETWEEN :startTime AND :endTime ORDER BY e.eventTime ASC")
    List<Event> findEventsBetween(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);

    // Find recently created events
    @Query("SELECT e FROM Event e WHERE e.createdAt > :recentTime ORDER BY e.createdAt DESC")
    List<Event> findRecentEvents(@Param("recentTime") Instant recentTime);

    // Find events created after a specific Instant
    List<Event> findByCreatedAtAfter(Instant createdAt);

    // Find events not yet cancelled
    @Query("SELECT e FROM Event e WHERE e.cancelledAt IS NULL ORDER BY e.eventTime ASC")
    List<Event> findActivEvents();

    // Find cancelled events
    @Query("SELECT e FROM Event e WHERE e.cancelledAt IS NOT NULL ORDER BY e.cancelledAt DESC")
    List<Event> findCancelledEvents();

    // Find events by status and creation time
    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.createdAt > :afterTime ORDER BY e.eventTime ASC")
    List<Event> findEventsByStatusAndCreatedAfter(@Param("status") String status, @Param("afterTime") Instant afterTime);
}