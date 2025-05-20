package com.backend.project_management.Service;

import com.backend.project_management.DTO.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO,String role,String email);
    List<RoleDTO> getAllRoles(String role, String email, String branchCode);
    RoleDTO getRoleById(Long id,String role,String email);
    void deleteRole(Long id,String role,String email);
}
