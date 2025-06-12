package com.backend.project_management.DTO;

import lombok.Data;
@Data
public class DepartmentDTO {

    private Long id;
    private String departmentName;
    private String createdByEmail;
    private String role;
    private String branchCode;
    private String createdDate;

}