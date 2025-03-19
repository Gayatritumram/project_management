package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.Entity.Project;
import com.backend.project_management.Mapper.ProjectMapper;
import com.backend.project_management.Repository.ProjectRepository;
import com.backend.project_management.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;



    @Override
    public ProjectDTO addProject(ProjectDTO projectDTO) {
        Project project =ProjectMapper.mapToProject(projectDTO);
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.mapToProjectDTO(savedProject);
    }
}
