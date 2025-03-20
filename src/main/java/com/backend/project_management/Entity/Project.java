package com.backend.project_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Project_Table")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectName;
    private String projectCategory;
    @Column(name = "statusBar", nullable = false)
    private double statusBar=0;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate estimatedDate;
    private String statusDescription;

    private String branch;
    private String department;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
