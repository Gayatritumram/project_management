package com.backend.project_management.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    private String department;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;
}
