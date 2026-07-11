package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "employee")
@NamedNativeQuery(
        query = """
                SELECT * FROM employee
                """,
        name = "Employee.findAllNative",
        resultSetMapping = "EmployeeMapping"
)
@SqlResultSetMapping(
        name = "EmployeeMapping",
        entities = {
                @EntityResult(
                        entityClass = Employee.class,
                        // New Jakarta Persistence 3.2 feature
                        lockMode = LockModeType.OPTIMISTIC
                )
        }
)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String department;
    private Double salary;
    @Version
    private Integer version;
}