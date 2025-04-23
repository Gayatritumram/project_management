package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    ProjectDTO addProject(ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long id);

    ProjectDTO getProjectByName(String name);

    List<ProjectDTO> getAllProjects();

    ProjectDTO updateProject(Long id, ProjectDTO project);

    void deleteProject(Long id);

    ProjectDTO assignProjectToTeam(Long projectId, Long teamId);


}
