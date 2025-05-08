package com.backend.project_management.DTO;

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
    private Long teamId;
    private String imageUrl;
    //private UserRole userRole;
}
