package com.backend.project_management.Service;

import com.backend.project_management.DTO.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentDTO createDepartment(DepartmentDTO dto, String email, String role);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO dto, String email, String role);
    DepartmentDTO getDepartmentById(Long id, String email, String role);
    List<DepartmentDTO> getAllDepartments(String role,String email, String branchCode);
}
