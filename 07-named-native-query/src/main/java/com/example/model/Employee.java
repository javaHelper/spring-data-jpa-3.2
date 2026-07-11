package com.example.model;

import com.example.dto.EmployeeSummary;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "employee")

@NamedNativeQuery(
        name = "Employee.findAllEntities",
        query = """
                SELECT *
                FROM employee
                """,
        resultClass = Employee.class
)
@NamedNativeQuery(
        name = "Employee.employeeSummary",
        query = """
                SELECT id,
                       name
                FROM employee
                """,
        resultSetMapping = "EmployeeSummaryMapping"
)
@NamedNativeQuery(
        name = "Employee.departmentStats",
        query = """
                SELECT department,
                       COUNT(*) AS totalEmployees
                FROM employee
                GROUP BY department
                """
)
@SqlResultSetMapping(
        name = "EmployeeSummaryMapping",
        classes = @ConstructorResult(
                targetClass = EmployeeSummary.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class)
                }
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private Long id;
    private String name;
    private String department;
    private BigDecimal salary;
}