package com.backend.project_management.DTO;

import com.backend.project_management.UserPermission.UserRole;
import lombok.Data;

@Data
public class ProjectAdminDTO{
    private String name;
    private String email;
    private String phone;
    private String password;
    private UserRole userRole1;


}
