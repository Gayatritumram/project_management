package com.backend.project_management.DTO;

import com.backend.project_management.Entity.Project;
import lombok.Data;

@Data
public class TeamDTO {
    private Long id;
    private String teamName;
    private String branch;
    private String department;
    private Project assignProject;
}