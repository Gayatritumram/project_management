package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectDTO;

import com.backend.project_management.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Project")
@CrossOrigin(origins = "https://pjsofttech.in")


public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/addProject")
    public ResponseEntity<ProjectDTO> addProject1(@RequestBody ProjectDTO projectDTO,
                                                  @RequestParam String role,
                                                  @RequestParam String email){
        ProjectDTO addProject = projectService.addProject(projectDTO,role,email);

        return new ResponseEntity<ProjectDTO>(addProject, HttpStatus.CREATED);
    }

    @GetMapping("/getProjectById/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id,
                                                     @RequestParam String role,
                                                     @RequestParam String email) {
        return ResponseEntity.ok(projectService.getProjectById(id,role,email));
    }

    @GetMapping("/getAllProjects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@ModelAttribute ProjectDTO filter,
                                                           @RequestParam String role,
                                                           @RequestParam String email) {
        return ResponseEntity.ok(projectService.getAllProjectsWithFilter(role, email, filter));
    }



    @GetMapping("/getProjectByName/{name}")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable String name,
                                                       @RequestParam String role,
                                                       @RequestParam String email) {
        return ResponseEntity.ok(projectService.getProjectByName(name,role,email));
    }


    @PutMapping("/updateProject/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id,
                                                    @RequestBody ProjectDTO project,
                                                    @RequestParam String role,
                                                    @RequestParam String email) {
        return ResponseEntity.ok(projectService.updateProject(id, project,role,email));
    }

    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id,
                                                @RequestParam String role,
                                                @RequestParam String email) {
        projectService.deleteProject(id,role,email);
        return ResponseEntity.ok("Project deleted successfully");
    }

    @PutMapping("/assignProjectToTeam/{projectId}/assign/{teamId}")
    public ResponseEntity<ProjectDTO> assignProjectToTeam(@PathVariable Long projectId,
                                                          @PathVariable Long teamId,
                                                          @RequestParam String role,
                                                          @RequestParam String email) {
        return ResponseEntity.ok(projectService.assignProjectToTeam(projectId, teamId,role,email));
    }
}
