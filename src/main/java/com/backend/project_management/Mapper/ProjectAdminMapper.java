package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import org.springframework.stereotype.Component;

@Component
public class ProjectAdminMapper {

    public static ProjectAdmin toEntity(ProjectAdminDTO adminDTO){
        ProjectAdmin admin = new ProjectAdmin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPhone(adminDTO.getPhone());
        admin.setPassword(adminDTO.getPassword());
        admin.setUserRole1(adminDTO.getUserRole1());
        return admin;
    }

    public static ProjectAdminDTO toDTO(ProjectAdmin admin){
        ProjectAdminDTO dto = new ProjectAdminDTO();
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        dto.setPassword(admin.getPassword());
        dto.setUserRole1(admin.getUserRole1());
        return dto;
    }
}
