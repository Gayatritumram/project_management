package com.backend.project_management.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "PM_TeamMember")
public class TeamMember{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public boolean isCanAccessTask() {
        return  true;
    }
}

