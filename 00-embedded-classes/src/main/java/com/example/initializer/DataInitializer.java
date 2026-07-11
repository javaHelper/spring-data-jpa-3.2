package com.example.initializer;

import com.example.entity.Address;
import com.example.entity.Company;
import com.example.entity.CompanyDetails;
import com.example.entity.ContactInfo;
import com.example.entity.User;
import com.example.repository.CompanyRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeCompanies();
    }

    private void initializeUsers() {
        Address addressNY = new Address(
            "123 Main St",
            "New York",
            "NY",
            "10001",
            "USA"
        );

        Address addressSF = new Address(
            "456 Tech Ave",
            "San Francisco",
            "CA",
            "94105",
            "USA"
        );

        ContactInfo personalContactJohn = new ContactInfo(
            "john.doe@personal.com",
            "555-0101",
            "www.johndoe.com"
        );

        ContactInfo workContactJohn = new ContactInfo(
            "john.doe@company.com",
            "555-0102",
            "john.doe.company.com"
        );

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUsername("johndoe");
        user1.setAddress(addressNY);
        user1.setPersonalContact(personalContactJohn);
        user1.setWorkContact(workContactJohn);
        user1.setJobTitle("Senior Software Engineer");
        user1.setDepartment("Engineering");

        ContactInfo personalContactJane = new ContactInfo(
            "jane.smith@personal.com",
            "555-0201",
            "www.janesmith.com"
        );

        ContactInfo workContactJane = new ContactInfo(
            "jane.smith@company.com",
            "555-0202",
            "jane.smith.company.com"
        );

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setUsername("janesmith");
        user2.setAddress(addressSF);
        user2.setPersonalContact(personalContactJane);
        user2.setWorkContact(workContactJane);
        user2.setJobTitle("Product Manager");
        user2.setDepartment("Product");

        userRepository.save(user1);
        userRepository.save(user2);

        System.out.println("✅ Users initialized successfully!");
    }

    private void initializeCompanies() {
        Address headquartersNY = new Address(
            "789 Business Plaza",
            "New York",
            "NY",
            "10002",
            "USA"
        );

        ContactInfo contactInfoNY = new ContactInfo(
            "info@techcorp.com",
            "1-800-TECH-CORP",
            "www.techcorp.com"
        );

        CompanyDetails detailsTechCorp = new CompanyDetails(
            "REG123456",
            "TAX987654",
            LocalDate.of(2010, 1, 15),
            "Technology"
        );

        Company company1 = new Company();
        company1.setName("TechCorp Inc");
        company1.setHeadquarters(headquartersNY);
        company1.setContact(contactInfoNY);
        company1.setDetails(detailsTechCorp);
        company1.setEmployeeCount(500);
        company1.setCeoName("Steve Johnson");

        Address headquartersSF = new Address(
            "321 Innovation Blvd",
            "San Francisco",
            "CA",
            "94106",
            "USA"
        );

        ContactInfo contactInfoSF = new ContactInfo(
            "contact@innovate.com",
            "1-888-INNOVATE",
            "www.innovate.com"
        );

        CompanyDetails detailsInnovate = new CompanyDetails(
            "REG654321",
            "TAX456789",
            LocalDate.of(2015, 6, 1),
            "Technology"
        );

        Company company2 = new Company();
        company2.setName("Innovate Solutions");
        company2.setHeadquarters(headquartersSF);
        company2.setContact(contactInfoSF);
        company2.setDetails(detailsInnovate);
        company2.setEmployeeCount(250);
        company2.setCeoName("Sarah Chen");

        companyRepository.save(company1);
        companyRepository.save(company2);

        System.out.println("✅ Companies initialized successfully!");
    }
}