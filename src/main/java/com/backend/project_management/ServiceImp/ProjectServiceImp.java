package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.Entity.Project;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.ProjectMapper;
import com.backend.project_management.Repository.ProjectRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeamRepository teamRepository;




    @Override
    public ProjectDTO addProject(ProjectDTO projectDTO) {
        Project project =ProjectMapper.mapToProject(projectDTO);

        if (projectDTO.getTeam1byID() != null) {
            Team admin = teamRepository.findById(projectDTO.getTeam1byID())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            project.setTeam1(admin);

        }
        Project savedTask = projectRepository.save(project);


        return ProjectMapper.mapToProjectDTO(savedTask);
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
        projectDTO.setBranchName(project.getBranchName());
        projectDTO.setTeam1byID(project.getTeam1byID());
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


        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        project.setTeam1(team);
        Project saved = projectRepository.save(project);


        return ProjectMapper.mapToProjectDTO(saved);
    }

}
