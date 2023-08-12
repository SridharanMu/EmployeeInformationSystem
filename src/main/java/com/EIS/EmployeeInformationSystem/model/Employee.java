package com.EIS.EmployeeInformationSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "employee_Id")
    private long employeeId;


    @Size(min = 5, max = 250)
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 5, max = 250)
    @Column(name = "last_name")
    private String lastName;
    @Size(min = 10, max = 11)
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    @Size(min = 10, max = 11)
    @Column(name = "date_of_joining")
    private String dateOfJoining;
    @Size(min = 2, max = 2)
    @Column(name = "grade")
    private String grade;
    @Column(name = "email")
    private String email;

}
