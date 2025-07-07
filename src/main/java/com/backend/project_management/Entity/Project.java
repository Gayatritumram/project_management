package com.backend.project_management.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PM_Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String projectName;
    private String projectCategory;
    @Column(name = "statusBar", nullable = false)
    private double statusBar=0;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate estimatedDate;
    private String statusDescription;

    private String branchName;
    private String departmentName;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;

    @ManyToOne
    @JoinColumn(name = "team1")
    private Team team1;
}
