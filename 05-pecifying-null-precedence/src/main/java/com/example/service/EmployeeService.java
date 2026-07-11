package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public List<Employee> getEmployees(boolean nullsFirst) {
        Sort.Order order = Sort.Order.asc("salary")
                .with(
                        nullsFirst ?
                                Sort.NullHandling.NULLS_FIRST :
                                Sort.NullHandling.NULLS_LAST
                );
        return repository.findAll(Sort.by(order));
    }
}