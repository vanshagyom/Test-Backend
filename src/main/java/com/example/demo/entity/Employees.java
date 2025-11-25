package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "testEmployees")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100,name = "name")
    private String name;

    @Column(nullable = false, length = 50, name = "designation")
    private String designation;

    @Column(nullable = false, length = 50, name = "department")
    private String department;

    @Column(nullable = false, name = "salary")
    private BigDecimal salary;

    @Column(nullable = false, name = "joining_date")
    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @Column(nullable = false, length = 20, name = "status")
    private String status;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalaryAudit> salaryAudits = new ArrayList<>();
}
