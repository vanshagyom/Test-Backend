package com.example.demo.service;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.dto.SalaryAdjustmentDTO;
import com.example.demo.entity.Employees;
import com.example.demo.entity.SalaryAudit;
import com.example.demo.handler.ResourceNotFoundException;
import com.example.demo.mapper.EmployeesMapper;
import com.example.demo.repository.EmployeesRepository;
import com.example.demo.repository.SalaryAuditRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Value("${employee.salary.max-increase-percent}")
    private BigDecimal maxIncreasePercent;

    @Value("${employee.salary.min}")
    private BigDecimal minSalary;

    @Value("${employee.salary.audit-enabled}")
    private boolean auditEnabled;

    @Value("${employee.pagination.default-page-size}")
    private int defaultPageSize;

    private final EmployeesRepository empRepo;
    private final SalaryAuditRepository salaryAuditRepository;

    @Autowired
    public EmployeesServiceImpl(EmployeesRepository empRepo, SalaryAuditRepository salaryAuditRepository) {
        this.empRepo = empRepo;
        this.salaryAuditRepository = salaryAuditRepository;
    }

    @Override
    public List<EmployeesDTO> getAllEmployees() {
        List<Employees> all = empRepo.findAll();
        return all.stream().map(EmployeesMapper::toDTO).toList();
    }

    @Override
    public EmployeesDTO getById(Long id) {
        Employees employee = empRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with id: " + id));
        return EmployeesMapper.toDTO(employee);
    }

    @Override
    public EmployeesDTO addNew(EmployeesDTO empDTO) {
        Employees entity = EmployeesMapper.toEntity(empDTO);
        Employees saved = empRepo.save(entity);
        return EmployeesMapper.toDTO(saved);
    }

    @Override
    public EmployeesDTO updateById(Long id, EmployeesDTO updateEmpDTO) {
        Employees updateEmp = empRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find employee with id: " + id));
        EmployeesMapper.updateEntityFromDTO(updateEmpDTO, updateEmp);
        Employees saved = empRepo.save(updateEmp);
        return EmployeesMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        empRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found id:" + id));
        empRepo.deleteById(id);
    }

    @Transactional
    @Override
    public EmployeesDTO adjustSalary(Long employeeId, SalaryAdjustmentDTO adjustmentDTO) {
        Employees employee = empRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        BigDecimal adjustmentPercent = adjustmentDTO.getAdjustmentPercent();
        if (adjustmentPercent.doubleValue() > maxIncreasePercent.doubleValue()) {
            throw new RuntimeException("Adjustment exceeds maximum allowed percent");
        }

        BigDecimal currentSalary = employee.getSalary();
        BigDecimal newSalary = currentSalary.add(
                currentSalary.multiply(adjustmentPercent).divide(BigDecimal.valueOf(100))
        );

        if (newSalary.doubleValue() < minSalary.doubleValue()) {
            throw new RuntimeException("Adjusted salary cannot be less than minimum salary");
        }

        employee.setSalary(newSalary);
        Employees updatedEmployee = empRepo.save(employee);

        if (auditEnabled) {
            SalaryAudit audit = new SalaryAudit();
            audit.setEmployee(employee);
            audit.setOldSalary(currentSalary);
            audit.setNewSalary(newSalary);
            audit.setAdjustmentPercent(adjustmentPercent);
            audit.setAdjustmentTime(LocalDateTime.now());
            audit.setReason(adjustmentDTO.getReason());
            salaryAuditRepository.save(audit);
        }

        return EmployeesMapper.toDTO(updatedEmployee);
    }

    @Override
    public Page<EmployeesDTO> getEmployees(String department,
                                           BigDecimal minSalary,
                                           BigDecimal maxSalary,
                                           String status,
                                           Integer page,
                                           Integer size,
                                           String sort) {

        int pageNumber = (page == null || page < 0) ? 0 : page;
        int pageSize = (size == null || size <= 0) ? defaultPageSize : size;

        // Sorting
        String sortField = "joiningDate";
        Sort.Direction sortDir = Sort.Direction.DESC;
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            sortField = snakeToCamel(parts[0]);
            if (parts.length > 1 && parts[1].equalsIgnoreCase("asc")) {
                sortDir = Sort.Direction.ASC;
            }
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDir, sortField));

        Page<Employees> employeePage = empRepo.findByFilters(
                department, minSalary, maxSalary, status, pageable
        );

        return employeePage.map(EmployeesMapper::toDTO);
    }

    private String snakeToCamel(String input) {
        StringBuilder result = new StringBuilder();
        boolean upper = false;
        for (char c : input.toCharArray()) {
            if (c == '_') {
                upper = true;
            } else {
                result.append(upper ? Character.toUpperCase(c) : c);
                upper = false;
            }
        }
        return result.toString();
    }
}
