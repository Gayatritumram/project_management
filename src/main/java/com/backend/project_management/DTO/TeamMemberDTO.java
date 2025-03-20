package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
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
    private String branch;
    private boolean isLeader = false;

    public TeamMemberDTO(boolean isLeader, String branch, String projectName, String role, String address, String phone, String department, LocalDate joinDate, String password, String email, String name, Long id) {
        this.isLeader = isLeader;
        this.branch = branch;
        this.projectName = projectName;
        this.role = role;
        this.address = address;
        this.phone = phone;
        this.department = department;
        this.joinDate = joinDate;
        this.password = password;
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public TeamMemberDTO(Long id, String name, String email, LocalDate joinDate, String department, String phone, String address, String role, String projectName, String branch, boolean leader) {
    }
}
