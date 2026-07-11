package com.example.controller;

import com.example.model.Employee;
import com.example.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping
    public List<Employee> getEmployees(@RequestParam(defaultValue = "false") boolean nullsFirst) {
        return service.getEmployees(nullsFirst);
    }
}