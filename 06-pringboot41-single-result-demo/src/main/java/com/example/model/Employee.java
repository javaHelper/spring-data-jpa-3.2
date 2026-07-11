package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "employee")
@NamedStoredProcedureQuery(
        name = "Employee.findByEmailSP",
        procedureName = "FIND_EMPLOYEE_BY_EMAIL",
        parameters = {@StoredProcedureParameter(mode = ParameterMode.IN, name = "EMAIL", type = String.class)},
        resultClasses = Employee.class
)
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;
}