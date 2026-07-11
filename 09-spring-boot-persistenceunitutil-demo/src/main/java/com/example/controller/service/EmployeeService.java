package com.example.controller.service;

import com.example.controller.dto.PersistenceUtilResponse;
import com.example.controller.entity.Employee;
import com.example.controller.repository.EmployeeRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.PersistenceUnitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private EmployeeRepository repository;

    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    public PersistenceUtilResponse inspect(Long id) {
        Employee emp = repository.getReferenceById(id);
        PersistenceUnitUtil util = emf.getPersistenceUnitUtil();

        Long version = (Long) util.getVersion(emp);
        boolean entityLoaded = util.isLoaded(emp);
        boolean profileLoaded = util.isLoaded(emp, "profile");
        util.load(emp);
        util.load(emp, "profile");

        boolean instance = util.isInstance(emp, Employee.class);
        String className = util.getClass(emp).getName();

        return new PersistenceUtilResponse(version, entityLoaded, profileLoaded, instance, className);
    }
}