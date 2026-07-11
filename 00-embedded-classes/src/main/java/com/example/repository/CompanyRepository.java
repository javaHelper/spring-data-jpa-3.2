package com.example.repository;

import com.example.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE c.headquarters.city = :city")
    List<Company> findCompaniesByHeadquartersCity(@Param("city") String city);

    @Query("SELECT c FROM Company c WHERE c.details.industry = :industry")
    List<Company> findCompaniesByIndustry(@Param("industry") String industry);
}