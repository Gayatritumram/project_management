package com.backend.project_management.Service;

import com.backend.project_management.DTO.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Long id);
    void deleteRole(Long id);
}
