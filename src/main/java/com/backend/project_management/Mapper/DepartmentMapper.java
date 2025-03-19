package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setDepartment(department.getDepartment());
        return dto;
    }

    public Department toEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setDepartment(dto.getDepartment());
        return department;
    }
}
