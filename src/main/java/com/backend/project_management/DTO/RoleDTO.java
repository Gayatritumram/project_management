package com.backend.project_management.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String roleName;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;
}
