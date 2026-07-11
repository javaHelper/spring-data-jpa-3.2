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
import java.time.Year;

@Entity
@Table(name = "publications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;

    // Year type - stores just the year (e.g., 2024)
    @Column(name = "publication_year", columnDefinition = "INT")
    private Year publicationYear;

    // Year for copyright
    @Column(name = "copyright_year", columnDefinition = "INT")
    private Year copyrightYear;

    private String publisher;
    private Integer pageCount;

    // When this record was added
    @Column(name = "added_at", columnDefinition = "TIMESTAMP")
    private Instant addedAt;

    // When this record was last updated
    @Column(name = "last_modified_at", columnDefinition = "TIMESTAMP")
    private Instant lastModifiedAt;

    private Double rating;
}