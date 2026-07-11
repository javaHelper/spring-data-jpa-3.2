package com.example.controller;

import com.example.service.entity.Publication;
import com.example.service.repository.PublicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Year;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PublicationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Publication testPublication;

    @BeforeEach
    void setUp() {
        publicationRepository.deleteAll();

        testPublication = new Publication();
        testPublication.setTitle("Spring Boot Test Book");
        testPublication.setAuthor("Test Author");
        testPublication.setIsbn("978-1234567890");
        testPublication.setPublicationYear(Year.of(2023));
        testPublication.setCopyrightYear(Year.of(2023));
        testPublication.setPublisher("Test Publisher");
        testPublication.setPageCount(400);
        testPublication.setRating(4.5);

        publicationRepository.save(testPublication);
    }

    @Test
    void testCreatePublication() throws Exception {
        Publication newPub = new Publication();
        newPub.setTitle("New Book");
        newPub.setAuthor("New Author");
        newPub.setIsbn("978-9876543210");
        newPub.setPublicationYear(Year.of(2024));
        newPub.setCopyrightYear(Year.of(2024));
        newPub.setPublisher("New Publisher");
        newPub.setPageCount(500);
        newPub.setRating(4.8);

        mockMvc.perform(post("/api/publications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPub)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title", is("New Book")))
            .andExpect(jsonPath("$.author", is("New Author")));
    }

    @Test
    void testGetPublicationsByYear() throws Exception {
        mockMvc.perform(get("/api/publications/year/2023")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$[0].publicationYear", is(2023)));
    }

    @Test
    void testGetPublicationsFromYear() throws Exception {
        mockMvc.perform(get("/api/publications/from-year/2020")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetPublicationsByYearRange() throws Exception {
        mockMvc.perform(get("/api/publications/year-range?startYear=2020&endYear=2024")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testGetByAuthorAndYear() throws Exception {
        mockMvc.perform(get("/api/publications/author/Test%20Author/year/2023")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$[0].author", is("Test Author")));
    }

    @Test
    void testGetHighRatedPublications() throws Exception {
        mockMvc.perform(get("/api/publications/high-rated/4.0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$[0].rating", greaterThanOrEqualTo(4.0)));
    }
}