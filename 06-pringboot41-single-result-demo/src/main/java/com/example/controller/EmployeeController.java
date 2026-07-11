package com.example.controller;

import com.example.model.Employee;
import com.example.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    public Employee save(@RequestBody Employee e){
        return service.save(e);
    }

    @GetMapping("/query/{id}")
    public Employee query(@PathVariable Long id){
        return service.findUsingQuery(id);
    }

    @GetMapping("/typed/{email}")
    public Employee typed(@PathVariable String email){
        return service.findUsingTypedQuery(email);
    }

    @GetMapping("/sp/{email}")
    public Employee sp(@PathVariable String email){
        return service.findUsingStoredProcedure(email);
    }
}