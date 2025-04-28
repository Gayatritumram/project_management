package com.backend.project_management.DTO;

import com.backend.project_management.Entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamMemberDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate joinDate;
    private String department;
    private String phone;
    private String address;
    private String role;
    private String projectName;
    private String branchName;

    private Long teamId;
    //private UserRole userRole;


}