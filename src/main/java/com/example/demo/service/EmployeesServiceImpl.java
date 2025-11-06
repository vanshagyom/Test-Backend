package com.example.demo.service;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.entity.Employees;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    private final EmployeesRepository empRepo;

    @Autowired
    public EmployeesServiceImpl(EmployeesRepository empRepo) {
        this.empRepo = empRepo;
    }

    @Override
    public List<Employees> getAllEmployees() {
        return empRepo.findAll();
    }

    @Override
    public Employees getById(Long id) {
        return empRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee with id: " + id + " not found."));
    }

    @Override
    public Employees addNew(EmployeesDTO empDTO) {
        Employees newEmployee  = new Employees();
        newEmployee.setDepartment(empDTO.getDepartment());
        newEmployee.setName(empDTO.getName());
        newEmployee.setSalary(empDTO.getSalary());
        newEmployee.setDesignation(empDTO.getDesignation());
        newEmployee.setJoiningDate(empDTO.getJoiningDate());
        newEmployee.setStatus(empDTO.getStatus());

        return empRepo.save(newEmployee);
    }

    @Override
    public Employees updateById(Long id, EmployeesDTO updateEmpDTO) {
        Employees updateEmp = empRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find employee with id: " + id));
        updateEmp.setDepartment(updateEmpDTO.getDepartment());
        updateEmp.setName(updateEmpDTO.getName());
        updateEmp.setSalary(updateEmpDTO.getSalary());
        updateEmp.setDesignation(updateEmpDTO.getDesignation());
        updateEmp.setJoiningDate(updateEmpDTO.getJoiningDate());
        updateEmp.setStatus(updateEmpDTO.getStatus());

        return empRepo.save(updateEmp);
    }

    @Override
    public void deleteById(Long id) {
        empRepo.findById(id).orElseThrow(()-> new RuntimeException("Not found id:" + id));
        empRepo.deleteById(id);
    }
}
