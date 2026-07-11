package com.example.controller.controller;

import com.example.controller.dto.PersistenceUtilResponse;
import com.example.controller.entity.Employee;
import com.example.controller.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService service;

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return service.create(employee);
    }

    @GetMapping("/{id}/inspect")
    public PersistenceUtilResponse inspect(@PathVariable Long id) {
        return service.inspect(id);
    }
}