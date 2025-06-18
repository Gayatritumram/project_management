package com.backend.project_management.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TeamLeaderDTO {
    private Long id;
    private String name;
    private String email;
    private String password; // hash this before saving
    private String phone;
    private String address;
    private String department;
    private String branchName;
    private LocalDate joinDate;

    private String imageUrl;

    @Email
    private String createdByEmail;

    private String role;

    private String branchCode;

    private Long teamId;

}
