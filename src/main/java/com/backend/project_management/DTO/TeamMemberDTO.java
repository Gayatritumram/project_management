package com.backend.project_management.DTO;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate joinDate;
    private String departmentName;
    private String phone;
    private String address;
    private String roleName;
    private String projectName;
    private String branchName;
    private String imageUrl;


    @Email
    private String createdByEmail;

    private String role;

    private String branchCode;

    private Long teamId;


}