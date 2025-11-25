package com.example.demo.repository;

import com.example.demo.entity.SalaryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryAuditRepository extends JpaRepository<SalaryAudit, Long> {
}
