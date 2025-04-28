package com.backend.project_management.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PMProject_Table")
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

    private String branchName;
    private String department;

    @ManyToOne
    @JoinColumn(name = "team1")
    private Team team1;
}
