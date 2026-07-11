package com.example.controller;

import com.example.dto.EmployeeSummary;
import com.example.model.Employee;
import com.example.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/entities")
    public List<Employee> entities() {
        return service.entities();
    }

    @GetMapping("/summary")
    public List<EmployeeSummary> summary() {
        return service.summaries();
    }

    @GetMapping("/stats")
    public List<Map<String, Object>> stats() {
        return service.stats()
                .stream()
                .map(r -> Map.of(
                        "department", r[0],
                        "count", r[1]
                ))
                .toList();
    }
}