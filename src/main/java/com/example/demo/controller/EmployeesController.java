package com.example.demo.controller;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.dto.SalaryAdjustmentDTO;
import com.example.demo.service.EmployeesService;
import com.example.demo.utils.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeesController {

    private final EmployeesService empService;

    @Autowired
    public EmployeesController(EmployeesService empService) {
        this.empService = empService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllEmployees(HttpServletRequest request) {
        List<EmployeesDTO> allEmployees = empService.getAllEmployees();
        return ResponseHandler.generateResponse("Employees Fetched Successfully", HttpStatus.OK, allEmployees, request);
    }

    @PostMapping
    public ResponseEntity<Object> addNewEmployee(
            @Valid @RequestBody EmployeesDTO empDto,
            HttpServletRequest request
    ) {
        EmployeesDTO newEmp = empService.addNew(empDto);
        return ResponseHandler.generateResponse("Added Successfully", HttpStatus.OK, newEmp, request);
    }

    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<Object> getEmployeeByID(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        EmployeesDTO employee = empService.getById(id);
        return ResponseHandler.generateResponse("Fetched Successfully", HttpStatus.OK, employee, request);
    }

    @PatchMapping("/updateEmployee/{id}")
    public ResponseEntity<Object> updateEmployeeById(
            @PathVariable Long id,
            @Valid @RequestBody EmployeesDTO empUpdateDto,
            HttpServletRequest request
    ) {
        EmployeesDTO updatedEmp = empService.updateById(id, empUpdateDto);
        return ResponseHandler.generateResponse("Updated Successfully", HttpStatus.OK, updatedEmp, request);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteEmployeeByID(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        empService.deleteById(id);
        return ResponseHandler.generateResponse("Deleted Successfully", HttpStatus.OK, null, request);
    }

    @PatchMapping("/{id}/adjust-salary")
    public ResponseEntity<Object> adjustSalary(
            @PathVariable Long id,
            @RequestBody SalaryAdjustmentDTO adjustmentDTO,
            HttpServletRequest request
    ) {
        EmployeesDTO updatedEmployee = empService.adjustSalary(id, adjustmentDTO);
        return ResponseHandler.generateResponse("Salary Audited Successfully", HttpStatus.OK, updatedEmployee, request);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) {
        Page<EmployeesDTO> employeePage = empService.getEmployees(
                department, minSalary, maxSalary, status, page, size, sort
        );

        // Wrap Page content + metadata in a stable structure
        var response = Map.of(
                "content", employeePage.getContent(),
                "pageNumber", employeePage.getNumber(),
                "pageSize", employeePage.getSize(),
                "totalElements", employeePage.getTotalElements(),
                "totalPages", employeePage.getTotalPages(),
                "isLast", employeePage.isLast()
        );

        return ResponseEntity.ok(response);
    }

}
