package com.example.service.service;

import com.example.service.entity.AuditLog;
import com.example.service.entity.Publication;
import com.example.service.repository.AuditLogRepository;
import com.example.service.repository.PublicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final AuditLogRepository auditLogRepository;

    public Publication createPublication(Publication publication) {
        publication.setAddedAt(Instant.now());
        publication.setLastModifiedAt(Instant.now());

        Publication saved = publicationRepository.save(publication);

        logAuditEvent("Publication", saved.getId(), "CREATE", "system", null, saved.getTitle());

        return saved;
    }

    public Optional<Publication> getPublicationById(Long id) {
        return publicationRepository.findById(id);
    }

    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    public List<Publication> getPublicationsByYear(Year year) {
        return publicationRepository.findByPublicationYear(year);
    }

    public List<Publication> getPublicationsFromYear(Year year) {
        return publicationRepository.findPublicationsFromYear(year);
    }

    public List<Publication> getPublicationsBetweenYears(Year startYear, Year endYear) {
        return publicationRepository.findPublicationsBetweenYears(startYear, endYear);
    }

    public List<Publication> getByAuthorAndYear(String author, Year year) {
        return publicationRepository.findByAuthorAndYear(author, year);
    }

    public List<Publication> getHighRatedPublications(Double minRating) {
        return publicationRepository.findHighRatedPublications(minRating);
    }

    public Publication updatePublication(Long id, Publication publicationDetails) {
        return publicationRepository.findById(id)
                .map(publication -> {
                    publication.setTitle(publicationDetails.getTitle());
                    publication.setAuthor(publicationDetails.getAuthor());
                    publication.setIsbn(publicationDetails.getIsbn());
                    publication.setPublicationYear(publicationDetails.getPublicationYear());
                    publication.setCopyrightYear(publicationDetails.getCopyrightYear());
                    publication.setPublisher(publicationDetails.getPublisher());
                    publication.setPageCount(publicationDetails.getPageCount());
                    publication.setRating(publicationDetails.getRating());
                    publication.setLastModifiedAt(Instant.now());

                    Publication updated = publicationRepository.save(publication);
                    logAuditEvent("Publication", id, "UPDATE", "system", publication.getTitle(), publicationDetails.getTitle());

                    return updated;
                })
                .orElseThrow(() -> new RuntimeException("Publication not found with id: " + id));
    }

    public void deletePublication(Long id) {
        publicationRepository.deleteById(id);
        logAuditEvent("Publication", id, "DELETE", "system", null, null);
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