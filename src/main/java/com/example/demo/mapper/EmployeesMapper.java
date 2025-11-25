package com.example.demo.mapper;

import com.example.demo.dto.EmployeesDTO;
import com.example.demo.entity.Employees;

public class EmployeesMapper {

    public static EmployeesDTO toDTO(Employees employee) {
        if (employee == null) {
            return null;
        }
        EmployeesDTO dto = new EmployeesDTO();
//        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setDesignation(employee.getDesignation());
        dto.setDepartment(employee.getDepartment());
        dto.setSalary(employee.getSalary());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setStatus(employee.getStatus());
        return dto;
    }

    public static Employees toEntity(EmployeesDTO dto) {
        if (dto == null) {
            return null;
        }
        Employees employee = new Employees();
//        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setDesignation(dto.getDesignation());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setStatus(dto.getStatus());
        return employee;
    }

    public static void updateEntityFromDTO(EmployeesDTO dto, Employees employee) {
        if (dto == null || employee == null) {
            return;
        }
        employee.setName(dto.getName());
        employee.setDesignation(dto.getDesignation());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setStatus(dto.getStatus());
    }
}
