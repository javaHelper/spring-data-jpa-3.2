package com.example.service.repository;

import com.example.service.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    
    // Find publications by publication year
    @Query("SELECT p FROM Publication p WHERE p.publicationYear = :year ORDER BY p.title ASC")
    List<Publication> findByPublicationYear(@Param("year") Year year);

    // Find publications from specific year onwards
    @Query("SELECT p FROM Publication p WHERE p.publicationYear >= :year ORDER BY p.publicationYear DESC, p.title ASC")
    List<Publication> findPublicationsFromYear(@Param("year") Year year);

    // Find publications from specific year range
    @Query("SELECT p FROM Publication p WHERE p.publicationYear BETWEEN :startYear AND :endYear ORDER BY p.publicationYear DESC, p.title ASC")
    List<Publication> findPublicationsBetweenYears(@Param("startYear") Year startYear, @Param("endYear") Year endYear);

    // Find recent publications
    @Query("SELECT p FROM Publication p WHERE p.addedAt > :recentTime ORDER BY p.addedAt DESC")
    List<Publication> findRecentAdditions(@Param("recentTime") java.time.Instant recentTime);

    // Find publications by author and year
    @Query("SELECT p FROM Publication p WHERE p.author = :author AND p.publicationYear = :year")
    List<Publication> findByAuthorAndYear(@Param("author") String author, @Param("year") Year year);

    // Find high-rated publications
    @Query("SELECT p FROM Publication p WHERE p.rating >= :minRating ORDER BY p.rating DESC, p.publicationYear DESC")
    List<Publication> findHighRatedPublications(@Param("minRating") Double minRating);
}