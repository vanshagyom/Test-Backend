package com.example.demo.service;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.entity.Employees;

import java.util.List;

public interface EmployeesService {
    List<Employees> getAllEmployees();
    Employees getById(Long id);
    Employees addNew(EmployeesDTO empDTO);
    Employees updateById(Long id, EmployeesDTO updateEmpDTO);
    void deleteById(Long id);
}
