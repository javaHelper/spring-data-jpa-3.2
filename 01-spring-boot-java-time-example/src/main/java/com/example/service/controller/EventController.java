package com.example.service.controller;

import com.example.service.entity.Event;
import com.example.service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
            .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/after/{hours}")
    public ResponseEntity<List<Event>> getEventsAfter(@PathVariable int hours) {
        Instant time = Instant.now().plus(hours, ChronoUnit.HOURS);
        return new ResponseEntity<>(eventService.getEventsAfter(time), HttpStatus.OK);
    }

    @GetMapping("/between")
    public ResponseEntity<List<Event>> getEventsBetween(@RequestParam String startTime, @RequestParam String endTime) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);
        return new ResponseEntity<>(eventService.getEventsBetween(start, end), HttpStatus.OK);
    }

    @GetMapping("/recent/{minutes}")
    public ResponseEntity<List<Event>> getRecentEvents(@PathVariable int minutes) {
        Instant recentTime = Instant.now().minus(minutes, ChronoUnit.MINUTES);
        return new ResponseEntity<>(eventService.getRecentEvents(recentTime), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Event>> getActiveEvents() {
        return new ResponseEntity<>(eventService.getActiveEvents(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return new ResponseEntity<>(eventService.updateEvent(id, eventDetails), HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long id) {
        eventService.cancelEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}