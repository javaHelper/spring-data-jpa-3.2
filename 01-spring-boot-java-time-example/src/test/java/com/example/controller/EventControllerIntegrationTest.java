package com.example.controller;

import com.example.service.entity.Event;
import com.example.service.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        testEvent = new Event();
        testEvent.setEventName("Test Conference");
        testEvent.setDescription("Test Description");
        testEvent.setEventTime(Instant.now().plus(14, ChronoUnit.DAYS));
        testEvent.setCreatedAt(Instant.now());
        testEvent.setUpdatedAt(Instant.now());
        testEvent.setLocation("Test City");
        testEvent.setStatus("SCHEDULED");

        eventRepository.save(testEvent);
    }

    @Test
    void testCreateEvent() throws Exception {
        Event newEvent = new Event();
        newEvent.setEventName("New Event");
        newEvent.setDescription("New Event Description");
        newEvent.setEventTime(Instant.now().plus(30, ChronoUnit.DAYS));
        newEvent.setLocation("New Location");
        newEvent.setStatus("SCHEDULED");

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEvent)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventName", is("New Event")))
            .andExpect(jsonPath("$.status", is("SCHEDULED")));
    }

    @Test
    void testGetEventById() throws Exception {
        Long eventId = testEvent.getId();

        mockMvc.perform(get("/api/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName", is("Test Conference")))
            .andExpect(jsonPath("$.location", is("Test City")));
    }

    @Test
    void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$[0].eventName", is("Test Conference")));
    }

    @Test
    void testUpdateEvent() throws Exception {
        Long eventId = testEvent.getId();
        testEvent.setEventName("Updated Conference");
        testEvent.setStatus("ONGOING");

        mockMvc.perform(put("/api/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEvent)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName", is("Updated Conference")))
            .andExpect(jsonPath("$.status", is("ONGOING")));
    }

    @Test
    void testCancelEvent() throws Exception {
        Long eventId = testEvent.getId();

        mockMvc.perform(post("/api/events/" + eventId + "/cancel")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/events/" + eventId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("CANCELLED")))
            .andExpect(jsonPath("$.cancelledAt", notNullValue()));
    }

    @Test
    void testDeleteEvent() throws Exception {
        Long eventId = testEvent.getId();

        mockMvc.perform(delete("/api/events/" + eventId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/events/" + eventId))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetRecentEvents() throws Exception {
        mockMvc.perform(get("/api/events/recent/1440") // Last 24 hours
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
}