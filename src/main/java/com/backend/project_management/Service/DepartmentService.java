package com.backend.project_management.Service;

import com.backend.project_management.DTO.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO,String role, String email);
    List<DepartmentDTO> getAllDepartments(String role,String email, String branchCode);
    DepartmentDTO getDepartmentById(Long id, String role, String email);
    void deleteDepartment(Long id,String role, String email);
}
