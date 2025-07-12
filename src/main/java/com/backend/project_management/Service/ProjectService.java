package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    ProjectDTO addProject(ProjectDTO projectDTO,String role,String email);

    ProjectDTO getProjectById(Long id,String role,String email);

    ProjectDTO getProjectByName(String name,String role,String email);

    List<ProjectDTO> getAllProjects(String role,String email,String branchCode);

    ProjectDTO updateProject(Long id, ProjectDTO project,String role,String email);

    void deleteProject(Long id,String role,String email);

    ProjectDTO assignProjectToTeam(Long projectId, Long teamId,String role,String email);

    List<ProjectDTO> getAllProjectsForDashboard(String role, String email, String branchCode);

    List<ProjectDTO> getAllProjectsWithFilter(String role, String email, ProjectDTO filter);
}
