package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.Entity.*;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.ProjectMapper;
import com.backend.project_management.Repository.BranchAdminRepository;
import com.backend.project_management.Repository.ProjectRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private StaffValidation  staffValidation;


    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private BranchAdminRepository adminRepo;



    @Override
    public ProjectDTO addProject(ProjectDTO projectDTO,String role,String email) {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create project");
        }
        System.out.println("Permission granted!");

        Project project =ProjectMapper.mapToProject(projectDTO);

        String branchCode = switch (role) {
            case "BRANCH" -> adminRepo.findByBranchEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };

        project.setRole(role);
        project.setCreatedByEmail(email);
        project.setBranchCode(branchCode);

        if (projectDTO.getTeam1() != null) {
            Team admin = teamRepository.findById(projectDTO.getTeam1())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            project.setTeam1(admin);

        }

        Project savedTask = projectRepository.save(project);
        System.out.println("project saved with id: " + savedTask.getId());

        return ProjectMapper.mapToProjectDTO(savedTask);
    }



    @Override
    public ProjectDTO getProjectById(Long id,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view project");
        }
        return ProjectMapper.mapToProjectDTO(projectRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Project not found with id: " + id)));
    }



    @Override
    public ProjectDTO getProjectByName(String name,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view project");
        }

        return ProjectMapper.mapToProjectDTO(projectRepository.findByProjectName(name)
                .orElseThrow(() -> new RequestNotFound("Project not found with name: " + name)));
    }




    @Override
    public List<ProjectDTO> getAllProjects(String role,String email,String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view project");
        }

        return projectRepository.findAllByBranchCode(branchCode).stream().map(ProjectMapper::mapToProjectDTO).collect(Collectors.toList());
    }





    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO project,String role,String email) {

        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to view project");
        }
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Project not found with id: " + id));

        existingProject.setProjectName(project.getProjectName());
        existingProject.setProjectCategory(project.getProjectCategory());
        existingProject.setStatusBar(project.getStatusBar());
        existingProject.setStatus(project.getStatus());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        existingProject.setEstimatedDate(project.getEstimatedDate());
        existingProject.setStatusDescription(project.getStatusDescription());
        existingProject.setBranchName(project.getBranchName());
        existingProject.setDepartmentName(project.getDepartmentName());



        return ProjectMapper.mapToProjectDTO(projectRepository.save(existingProject));
    }




    @Override
    public void deleteProject(Long id,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view project");
        }
        projectRepository.deleteById(id);
    }




    @Override
    public ProjectDTO assignProjectToTeam(Long projectId, Long teamId,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to view project");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        project.setTeam1(team);
        Project saved = projectRepository.save(project);


        return ProjectMapper.mapToProjectDTO(saved);
    }

}
