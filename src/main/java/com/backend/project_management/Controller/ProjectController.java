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

public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/addProject")
    public ResponseEntity<ProjectDTO> addProject(@RequestBody ProjectDTO projectDTO){
        ProjectDTO addProject = projectService.addProject(projectDTO);
        return new ResponseEntity<ProjectDTO>(addProject, HttpStatus.CREATED);
    }

    @GetMapping("getProjectById/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("updateProject/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO project) {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @DeleteMapping("deleteProject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project Deleted");
    }

    @PutMapping("assignProjectToTeam/{projectId}/assign/{teamId}")
    public ResponseEntity<ProjectDTO> assignProjectToTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        return ResponseEntity.ok(projectService.assignProjectToTeam(projectId, teamId));
    }



}
