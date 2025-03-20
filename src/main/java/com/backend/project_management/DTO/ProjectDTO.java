package com.backend.project_management.DTO;


import com.backend.project_management.Entity.Project;
import com.backend.project_management.Entity.Team;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String projectCategory;
    private double statusBar=0;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate estimatedDate;
    private String statusDescription;
    private String branch;
    private String department;
    private Team teamName;
}








