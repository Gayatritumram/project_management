package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectDTO;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    ProjectDTO addProject(ProjectDTO projectDTO);
}
