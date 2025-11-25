package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salary_audit")
public class SalaryAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employee;

    @Column(nullable = false)
    private BigDecimal oldSalary;

    @Column(nullable = false)
    private BigDecimal newSalary;

    @Column(nullable = false)
    private BigDecimal adjustmentPercent;

    @Column(length = 255)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime adjustmentTime;
}
