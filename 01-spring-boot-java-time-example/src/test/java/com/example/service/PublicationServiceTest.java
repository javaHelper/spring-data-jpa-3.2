package com.example.service;

import com.example.service.entity.Publication;
import com.example.service.repository.AuditLogRepository;
import com.example.service.repository.PublicationRepository;
import com.example.service.service.PublicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationServiceTest {
    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private PublicationService publicationService;

    private Publication testPublication;

    @BeforeEach
    void setUp() {
        testPublication = new Publication();
        testPublication.setId(1L);
        testPublication.setTitle("Test Book");
        testPublication.setAuthor("Test Author");
        testPublication.setIsbn("978-1234567890");
        testPublication.setPublicationYear(Year.of(2023));
        testPublication.setCopyrightYear(Year.of(2023));
        testPublication.setPublisher("Test Publisher");
        testPublication.setPageCount(300);
        testPublication.setRating(4.5);
    }

    @Test
    void testCreatePublication() {
        when(publicationRepository.save(any(Publication.class))).thenReturn(testPublication);

        Publication created = publicationService.createPublication(testPublication);

        assertNotNull(created);
        assertEquals("Test Book", created.getTitle());
        assertEquals(Year.of(2023), created.getPublicationYear());
        verify(publicationRepository, times(1)).save(any(Publication.class));
    }

    @Test
    void testGetPublicationById() {
        when(publicationRepository.findById(1L)).thenReturn(Optional.of(testPublication));

        Optional<Publication> found = publicationService.getPublicationById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Book", found.get().getTitle());
        verify(publicationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPublicationsByYear() {
        Year year = Year.of(2023);
        when(publicationRepository.findByPublicationYear(year)).thenReturn(java.util.List.of(testPublication));

        var publications = publicationService.getPublicationsByYear(year);

        assertFalse(publications.isEmpty());
        assertEquals(1, publications.size());
        verify(publicationRepository, times(1)).findByPublicationYear(year);
    }

    @Test
    void testGetByAuthorAndYear() {
        Year year = Year.of(2023);
        when(publicationRepository.findByAuthorAndYear("Test Author", year))
            .thenReturn(java.util.List.of(testPublication));

        var publications = publicationService.getByAuthorAndYear("Test Author", year);

        assertFalse(publications.isEmpty());
        assertEquals("Test Author", publications.get(0).getAuthor());
        verify(publicationRepository, times(1)).findByAuthorAndYear("Test Author", year);
    }
}