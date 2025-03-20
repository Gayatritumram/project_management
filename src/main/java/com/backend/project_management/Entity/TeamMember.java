package com.backend.project_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TeamMember_table")
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

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}

