package com.backend.project_management.DTO;

import com.backend.project_management.UserPermission.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectAdminDTO{
    private String name;
    private String email;
    private String phone;
    private String password;
    private UserRole userRole1;
    
    
    
}
