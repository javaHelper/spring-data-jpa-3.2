package com.example.service.controller;

import com.example.service.entity.Publication;
import com.example.service.service.PublicationService;
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

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/publications")
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationService publicationService;

    @PostMapping
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) {
        return new ResponseEntity<>(publicationService.createPublication(publication), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable Long id) {
        return publicationService.getPublicationById(id)
                .map(pub -> new ResponseEntity<>(pub, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Publication>> getAllPublications() {
        return new ResponseEntity<>(publicationService.getAllPublications(), HttpStatus.OK);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Publication>> getPublicationsByYear(@PathVariable int year) {
        List<Publication> pubs = publicationService.getPublicationsByYear(Year.of(year));
        return new ResponseEntity<>(pubs, HttpStatus.OK);
    }

    @GetMapping("/from-year/{year}")
    public ResponseEntity<List<Publication>> getPublicationsFromYear(@PathVariable int year) {
        List<Publication> pubs = publicationService.getPublicationsFromYear(Year.of(year));
        return new ResponseEntity<>(pubs, HttpStatus.OK);
    }

    @GetMapping("/year-range")
    public ResponseEntity<List<Publication>> getPublicationsBetweenYears(@RequestParam int startYear, @RequestParam int endYear) {
        List<Publication> pubs = publicationService.getPublicationsBetweenYears(Year.of(startYear), Year.of(endYear));
        return new ResponseEntity<>(pubs, HttpStatus.OK);
    }

    @GetMapping("/author/{author}/year/{year}")
    public ResponseEntity<List<Publication>> getByAuthorAndYear(@PathVariable String author, @PathVariable int year) {
        List<Publication> pubs = publicationService.getByAuthorAndYear(author, Year.of(year));
        return new ResponseEntity<>(pubs, HttpStatus.OK);
    }

    @GetMapping("/high-rated/{minRating}")
    public ResponseEntity<List<Publication>> getHighRated(@PathVariable Double minRating) {
        return new ResponseEntity<>(publicationService.getHighRatedPublications(minRating), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(@PathVariable Long id, @RequestBody Publication publicationDetails) {
        return new ResponseEntity<>(publicationService.updatePublication(id, publicationDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}