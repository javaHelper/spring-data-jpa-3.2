package com.example.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "email", column = @Column(name = "personal_email")),
        @AttributeOverride(name = "phone", column = @Column(name = "personal_phone")),
        @AttributeOverride(name = "website", column = @Column(name = "personal_website"))
    })
    private ContactInfo personalContact;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "email", column = @Column(name = "work_email")),
        @AttributeOverride(name = "phone", column = @Column(name = "work_phone")),
        @AttributeOverride(name = "website", column = @Column(name = "work_website"))
    })
    private ContactInfo workContact;

    private String department;
    private String jobTitle;
}