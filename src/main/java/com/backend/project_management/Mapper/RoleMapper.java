package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.RoleDTO;
import com.backend.project_management.Entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        return dto;
    }

    public Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setRoleName(dto.getRoleName());
        return role;
    }
}
