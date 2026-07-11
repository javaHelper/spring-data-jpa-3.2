package com.example.service;

import com.example.dto.EmployeeSummary;
import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public List<Employee> entities() {
        return repository.entities();
    }

    public List<EmployeeSummary> summaries() {
        return repository.summary();
    }

    public List<Object[]> stats() {
        return repository.departmentStats();
    }
}