package com.backend.project_management.DTO;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class ProjectAdminDTO{
    private String name;
    private String email;
    private String phone;
    private String password;
    @Transient
    private String cpassword;

}
