package com.example.service;

import com.example.service.entity.Event;
import com.example.service.repository.AuditLogRepository;
import com.example.service.repository.EventRepository;
import com.example.service.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setEventName("Test Event");
        testEvent.setDescription("Test Description");
        testEvent.setEventTime(Instant.now().plus(7, ChronoUnit.DAYS));
        testEvent.setLocation("Test Location");
        testEvent.setStatus("SCHEDULED");
    }

    @Test
    void testCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event created = eventService.createEvent(testEvent);

        assertNotNull(created);
        assertEquals("Test Event", created.getEventName());
        assertEquals("SCHEDULED", created.getStatus());
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        Optional<Event> found = eventService.getEventById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Event", found.get().getEventName());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEventsAfter() {
        Instant afterTime = Instant.now();
        when(eventRepository.findEventsAfter(afterTime)).thenReturn(java.util.List.of(testEvent));

        var events = eventService.getEventsAfter(afterTime);

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findEventsAfter(afterTime);
    }

    @Test
    void testCancelEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        eventService.cancelEvent(1L);

        assertEquals("CANCELLED", testEvent.getStatus());
        assertNotNull(testEvent.getCancelledAt());
        verify(eventRepository, times(1)).save(testEvent);
    }
}