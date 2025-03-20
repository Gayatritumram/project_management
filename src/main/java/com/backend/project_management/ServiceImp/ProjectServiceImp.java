package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Project;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.ProjectMapper;
import com.backend.project_management.Repository.ProjectRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    private TeamRepository teamRepository;



    @Override
    public ProjectDTO addProject(ProjectDTO projectDTO) {
        Project project =ProjectMapper.mapToProject(projectDTO);
        project.setEstimatedDate(LocalDate.now());
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.mapToProjectDTO(savedProject);
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        return ProjectMapper.mapToProjectDTO(projectRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Project not found with id: " + id)));
    }

    @Override
    public ProjectDTO getProjectByName(String name) {
        return ProjectMapper.mapToProjectDTO(projectRepository.findByProjectName(name)
                .orElseThrow(() -> new RequestNotFound("Project not found with name: " + name)));
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream().map(ProjectMapper::mapToProjectDTO).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO project) {
        ProjectDTO projectDTO = getProjectById(id);
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectCategory(project.getProjectCategory());
        projectDTO.setStatusBar(project.getStatusBar());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setEstimatedDate(project.getEstimatedDate());
        projectDTO.setStatusDescription(project.getStatusDescription());
        projectDTO.setBranch(project.getBranch());
        projectDTO.setTeam(project.getTeam());
        projectDTO.setDepartment(project.getDepartment());

        Project project1 = ProjectMapper.mapToProject(projectDTO);
        return ProjectMapper.mapToProjectDTO(projectRepository.save(project1));
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectDTO assignProjectToTeam(Long projectId, Long teamId) {
        ProjectDTO project = getProjectById(projectId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        project.setTeam(team);
        Project project1 = ProjectMapper.mapToProject(project); // Assign team to project
        return ProjectMapper.mapToProjectDTO(projectRepository.save(project1));
    }
}
