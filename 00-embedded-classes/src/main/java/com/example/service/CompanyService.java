package com.example.service;

import com.example.entity.Company;
import com.example.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company updateCompany(Long id, Company companyDetails) {
        return companyRepository.findById(id)
                .map(company -> {
                    company.setName(companyDetails.getName());
                    company.setHeadquarters(companyDetails.getHeadquarters());
                    company.setContact(companyDetails.getContact());
                    company.setDetails(companyDetails.getDetails());
                    company.setEmployeeCount(companyDetails.getEmployeeCount());
                    company.setCeoName(companyDetails.getCeoName());
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public List<Company> getCompaniesByHeadquartersCity(String city) {
        return companyRepository.findCompaniesByHeadquartersCity(city);
    }

    public List<Company> getCompaniesByIndustry(String industry) {
        return companyRepository.findCompaniesByIndustry(industry);
    }
}
