package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Entity.Department;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DepartmentMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setCreatedByEmail(department.getCreatedByEmail());
        dto.setRole(department.getRole());
        dto.setBranchCode(department.getBranchCode());

        if (department.getCreatedDate() != null) {
            dto.setCreatedDate(department.getCreatedDate().format(formatter));
        }
        return dto;
    }

    public Department toEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setDepartmentName(dto.getDepartmentName());
        // Do not set createdByEmail, role, branchCode or createdDate here — these are set in service
        return department;
    }

}
