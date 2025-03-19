package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.Entity.Project;
import com.backend.project_management.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
