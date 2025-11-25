package com.example.demo.repository;

import com.example.demo.entity.Employees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long> {

    @Query("""
SELECT e FROM Employees e
WHERE (:department IS NULL OR :department = '' OR LOWER(e.department) = LOWER(:department))
  AND (:minSalary IS NULL OR e.salary >= :minSalary)
  AND (:maxSalary IS NULL OR e.salary <= :maxSalary)
  AND (:status IS NULL OR LOWER(e.status) = LOWER(:status))
""")
    Page<Employees> findByFilters(@Param("department") String department,
                                  @Param("minSalary") BigDecimal minSalary,
                                  @Param("maxSalary") BigDecimal maxSalary,
                                  @Param("status") String status,
                                  Pageable pageable);
}
