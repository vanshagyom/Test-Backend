package com.example.demo.service;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.dto.SalaryAdjustmentDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeesService {
    List<EmployeesDTO> getAllEmployees();
    EmployeesDTO getById(Long id);
    EmployeesDTO addNew(EmployeesDTO empDTO);
    EmployeesDTO updateById(Long id, EmployeesDTO updateEmpDTO);
    void deleteById(Long id);
    EmployeesDTO adjustSalary(Long employeeId, SalaryAdjustmentDTO adjustmentDTO);

    Page<EmployeesDTO> getEmployees(
            String department,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String status,
            Integer page,
            Integer size,
            String sort
    );
}
