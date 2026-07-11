package com.example.repository;

import com.example.dto.EmployeeSummary;
import com.example.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Employee> entities() {
        return em.createNamedQuery("Employee.findAllEntities", Employee.class)
                .getResultList();
    }

    public List<EmployeeSummary> summary() {
        return em.createNamedQuery("Employee.employeeSummary", EmployeeSummary.class)
                .getResultList();
    }

    public List<Object[]> departmentStats() {
        return em.createNamedQuery("Employee.departmentStats").getResultList();
    }
}