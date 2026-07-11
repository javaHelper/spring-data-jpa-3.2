package com.example.service.initializer;

import com.example.service.entity.AuditLog;
import com.example.service.entity.Event;
import com.example.service.entity.Publication;
import com.example.service.repository.AuditLogRepository;
import com.example.service.repository.EventRepository;
import com.example.service.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EventRepository eventRepository;
    private final PublicationRepository publicationRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeEvents();
        initializePublications();
        initializeAuditLogs();
    }

    private void initializeEvents() {
        Event event1 = new Event();
        event1.setEventName("Spring Boot Conference 2024");
        event1.setDescription("Annual Spring Boot conference");
        event1.setEventTime(Instant.now().plus(30, ChronoUnit.DAYS));
        event1.setCreatedAt(Instant.now());
        event1.setUpdatedAt(Instant.now());
        event1.setLocation("San Francisco, CA");
        event1.setStatus("SCHEDULED");

        Event event2 = new Event();
        event2.setEventName("Java Workshop");
        event2.setDescription("Advanced Java programming");
        event2.setEventTime(Instant.now().plus(7, ChronoUnit.DAYS));
        event2.setCreatedAt(Instant.now().minus(10, ChronoUnit.DAYS));
        event2.setUpdatedAt(Instant.now());
        event2.setLocation("New York, NY");
        event2.setStatus("SCHEDULED");

        eventRepository.save(event1);
        eventRepository.save(event2);

        System.out.println("✅ Events initialized successfully!");
    }

    private void initializePublications() {
        Publication pub1 = new Publication();
        pub1.setTitle("Spring Boot in Action");
        pub1.setAuthor("Craig Walls");
        pub1.setIsbn("978-1617291517");
        pub1.setPublicationYear(Year.of(2015));
        pub1.setCopyrightYear(Year.of(2015));
        pub1.setPublisher("Manning");
        pub1.setPageCount(600);
        pub1.setAddedAt(Instant.now().minus(60, ChronoUnit.DAYS));
        pub1.setLastModifiedAt(Instant.now());
        pub1.setRating(4.8);

        Publication pub2 = new Publication();
        pub2.setTitle("Modern Java in Action");
        pub2.setAuthor("Raoul-Gabriel Urma");
        pub2.setIsbn("978-1617293566");
        pub2.setPublicationYear(Year.of(2018));
        pub2.setCopyrightYear(Year.of(2018));
        pub2.setPublisher("Manning");
        pub2.setPageCount(600);
        pub2.setAddedAt(Instant.now().minus(30, ChronoUnit.DAYS));
        pub2.setLastModifiedAt(Instant.now());
        pub2.setRating(4.7);

        Publication pub3 = new Publication();
        pub3.setTitle("Java Concurrency in Practice");
        pub3.setAuthor("Brian Goetz");
        pub3.setIsbn("978-0321349606");
        pub3.setPublicationYear(Year.of(2006));
        pub3.setCopyrightYear(Year.of(2006));
        pub3.setPublisher("Addison-Wesley");
        pub3.setPageCount(384);
        pub3.setAddedAt(Instant.now().minus(120, ChronoUnit.DAYS));
        pub3.setLastModifiedAt(Instant.now());
        pub3.setRating(4.9);

        publicationRepository.save(pub1);
        publicationRepository.save(pub2);
        publicationRepository.save(pub3);

        System.out.println("✅ Publications initialized successfully!");
    }

    private void initializeAuditLogs() {
        AuditLog log1 = new AuditLog();
        log1.setEntityType("Event");
        log1.setEntityId(1L);
        log1.setAction("CREATE");
        log1.setChangedBy("system");
        log1.setChangeTimestamp(Instant.now().minus(60, ChronoUnit.DAYS));
        log1.setAuditYear(Year.now());
        log1.setOldValue(null);
        log1.setNewValue("Spring Boot Conference 2024");
        log1.setDescription("New event created");

        AuditLog log2 = new AuditLog();
        log2.setEntityType("Publication");
        log2.setEntityId(1L);
        log2.setAction("CREATE");
        log2.setChangedBy("system");
        log2.setChangeTimestamp(Instant.now().minus(60, ChronoUnit.DAYS));
        log2.setAuditYear(Year.now());
        log2.setOldValue(null);
        log2.setNewValue("Spring Boot in Action");
        log2.setDescription("New publication added");

        auditLogRepository.save(log1);
        auditLogRepository.save(log2);

        System.out.println("✅ Audit logs initialized successfully!");
    }
}