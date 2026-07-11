package com.example.service.service;

import com.example.service.entity.AuditLog;
import com.example.service.entity.Event;
import com.example.service.repository.AuditLogRepository;
import com.example.service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final AuditLogRepository auditLogRepository;

    public Event createEvent(Event event) {
        event.setCreatedAt(Instant.now());
        event.setUpdatedAt(Instant.now());
        event.setStatus("SCHEDULED");

        Event saved = eventRepository.save(event);

        // Log the creation
        logAuditEvent("Event", saved.getId(), "CREATE", "system", null, saved.getEventName());

        return saved;
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsAfter(Instant time) {
        return eventRepository.findEventsAfter(time);
    }

    public List<Event> getEventsBetween(Instant startTime, Instant endTime) {
        return eventRepository.findEventsBetween(startTime, endTime);
    }

    public List<Event> getRecentEvents(Instant recentTime) {
        return eventRepository.findRecentEvents(recentTime);
    }

    public List<Event> getActiveEvents() {
        return eventRepository.findActivEvents();
    }

    public Event updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setEventName(eventDetails.getEventName());
                    event.setDescription(eventDetails.getDescription());
                    event.setEventTime(eventDetails.getEventTime());
                    event.setLocation(eventDetails.getLocation());
                    event.setStatus(eventDetails.getStatus());
                    event.setUpdatedAt(Instant.now());

                    Event updated = eventRepository.save(event);
                    logAuditEvent("Event", id, "UPDATE", "system", event.getStatus(), eventDetails.getStatus());

                    return updated;
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public void cancelEvent(Long id) {
        eventRepository.findById(id)
                .ifPresent(event -> {
                    event.setStatus("CANCELLED");
                    event.setCancelledAt(Instant.now());
                    event.setUpdatedAt(Instant.now());
                    eventRepository.save(event);
                    logAuditEvent("Event", id, "DELETE", "system", "SCHEDULED", "CANCELLED");
                });
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
        logAuditEvent("Event", id, "DELETE", "system", null, null);
    }

    private void logAuditEvent(String entityType, Long entityId, String action, String changedBy, String oldValue, String newValue) {
        AuditLog log = new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setChangedBy(changedBy);
        log.setChangeTimestamp(Instant.now());
        log.setAuditYear(Year.now());
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        auditLogRepository.save(log);
    }
}