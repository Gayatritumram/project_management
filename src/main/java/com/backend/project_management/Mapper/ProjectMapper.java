package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.Entity.Project;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProjectMapper {
    //Project Mapper
    public static ProjectDTO mapToProjectDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getProjectName(),
                project.getProjectCategory(),
                project.getStatusBar(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                project.getEstimatedDate(),
                project.getStatusDescription(),
                project.getBranch(),
                project.getDepartment()

        );

    }

    public static Project mapToProject(ProjectDTO projectDTO){
        return new Project(
                projectDTO.getId(),
                projectDTO.getProjectName(),
                projectDTO.getProjectCategory(),
                projectDTO.getStatusBar(),
                projectDTO.getStatus(),
                projectDTO.getStartDate(),
                projectDTO.getEndDate(),
                projectDTO.getEstimatedDate(),
                projectDTO.getStatusDescription(),
                projectDTO.getBranch(),
                projectDTO.getDepartment()

                );
    }


}
