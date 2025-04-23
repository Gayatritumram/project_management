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
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectCategory(project.getProjectCategory());
        projectDTO.setStatusBar(project.getStatusBar());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEstimatedDate(project.getEstimatedDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setStatusDescription(project.getStatusDescription());
        projectDTO.setBranchName(project.getBranchName());
        projectDTO.setDepartment(project.getDepartment());
        if (project.getTeam1() != null) {
            projectDTO.setTeam1byID(project.getTeam1().getId());
        }
        return projectDTO;

    }

    public static Project mapToProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectCategory(projectDTO.getProjectCategory());
        project.setStatusBar(projectDTO.getStatusBar());
        project.setStatus(projectDTO.getStatus());
        project.setStartDate(projectDTO.getStartDate());
        project.setEstimatedDate(projectDTO.getEstimatedDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatusDescription(projectDTO.getStatusDescription());
        project.setBranchName(projectDTO.getBranchName());
        project.setDepartment(projectDTO.getDepartment());


        return project;
    }

}
