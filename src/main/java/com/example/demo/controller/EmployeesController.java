package com.example.demo.controller;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.entity.Employees;
import com.example.demo.service.EmployeesService;
import com.example.demo.utils.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Object> getAllEmployees(HttpServletRequest request){
        List<Employees> allEmployees = empService.getAllEmployees();
        return ResponseHandler.generateResponse("Orders Fetched Successfully", HttpStatus.OK, allEmployees, request);
    }

    @PostMapping
    public ResponseEntity<Object> addNewEmployee( @Valid @RequestBody EmployeesDTO empDto, HttpServletRequest request){
        Employees newEmp = empService.addNew(empDto);
        return ResponseHandler.generateResponse("Added Successfully", HttpStatus.OK, newEmp, request);
    }

    @GetMapping("getEmployeeById/{id}")
    public ResponseEntity<Object> getEmployeeByID(@PathVariable Long id, HttpServletRequest request){
        Employees byIdEmployee = empService.getById(id);
        return ResponseHandler.generateResponse("Fetched Successfully", HttpStatus.OK, byIdEmployee, request);
    }

    @PatchMapping ("updateEmployee/{myId}")
    public ResponseEntity<Object> updateEmployeeById(@PathVariable Long myId, @Valid @RequestBody EmployeesDTO empUpdateDto, HttpServletRequest request){
        Employees updateEmp = empService.updateById(myId, empUpdateDto);
        return ResponseHandler.generateResponse("Updated Successfully", HttpStatus.OK, updateEmp, request);
    }

    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<Object> deleteEmployeeByID(@PathVariable Long id, HttpServletRequest request){
        empService.deleteById(id);
        return ResponseHandler.generateResponse("Deleted Successfully", HttpStatus.OK, null, request);
    }
}
