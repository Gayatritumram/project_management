package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProjectAdminDTO{
    private String name;
    private String email;
    private String phone;
    private String password;
    private String cpassword;

}
