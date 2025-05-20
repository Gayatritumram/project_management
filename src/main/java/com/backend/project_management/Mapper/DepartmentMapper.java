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
        dto.setRole(department.getRole());
        dto.setCreatedByEmail(department.getCreatedByEmail());
        dto.setBranchCode(department.getBranchCode());
        return dto;
    }

    public Department toEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setDepartment(dto.getDepartment());
        department.setRole(dto.getRole());
        department.setCreatedByEmail(dto.getCreatedByEmail());
        department.setBranchCode(dto.getBranchCode());
        return department;
    }
}
