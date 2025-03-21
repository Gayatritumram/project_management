package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Service.ProjectService;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Create")
public class CreateController {
    @Autowired
    private TeamMemberService service;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/createTeamMember")
    public TeamMemberDTO create(@RequestBody TeamMemberDTO dto) {
        return service.createTeamMember(dto);
    }

    @PostMapping("/addProject")
    public ResponseEntity<ProjectDTO> addProject1(@RequestBody ProjectDTO projectDTO){
        ProjectDTO addProject = projectService.addProject(projectDTO);
        return new ResponseEntity<ProjectDTO>(addProject, HttpStatus.CREATED);
    }

}
