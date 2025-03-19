package com.backend.project_management.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private LocalDate joinDate;
    private String department;
    private String phone;
    private String address;
    private String role;
    private String projectName;
    private String branch;
    private boolean isLeader = false;
    //default value is false
}

