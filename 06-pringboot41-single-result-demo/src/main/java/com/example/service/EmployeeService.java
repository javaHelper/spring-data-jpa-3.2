package com.example.service;

import com.example.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {
    @PersistenceContext
    private EntityManager entityManager;

    public Employee save(Employee employee) {
        return entityManager.merge(employee);
    }

    public Employee findUsingQuery(Long id) {
        Query query = entityManager.createQuery("select e from Employee e where e.id=:id");
        query.setParameter("id", id);
        return (Employee) query.getSingleResultOrNull();
    }

    public Employee findUsingTypedQuery(String email) {
        TypedQuery<Employee> query =
                entityManager.createQuery(
                        "select e from Employee e where e.email=:email",
                        Employee.class);

        query.setParameter("email", email);
        return query.getSingleResultOrNull();
    }

    public Employee findUsingStoredProcedure(String email) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Employee.findByEmailSP");
        query.setParameter("EMAIL", email);
        return (Employee) query.getSingleResultOrNull();
    }
}