package com.example.repository;

import com.example.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Employee> findAllNative() {
        return entityManager
                .createNamedQuery("Employee.findAllNative", Employee.class)
                .getResultList();
    }
}